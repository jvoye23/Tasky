@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.EditTextType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.getNextHalfMarkLocalTime
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toEpochMilliseconds
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toLocalDateTime
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.UUID.randomUUID
import kotlin.time.ExperimentalTime

class AgendaDetailScreenViewModel(
    private val isEdit: Boolean,
    private val taskyType: TaskyType,
    private val taskyItemId: String?,
    private val agendaRepository: AgendaRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(AgendaDetailState(
        titleText = savedStateHandle["titleText"],
        descriptionText = savedStateHandle["descriptionText"],
        isEditMode = savedStateHandle.get<Boolean>("isEditMode") ?: false,
        selectedDateMillis = savedStateHandle.get<Long>("selectedDateMillis") ?: getNextHalfMarkLocalTime().toEpochMilliseconds(),
        notificationType = savedStateHandle.get<NotificationType>("notificationType") ?: NotificationType.THIRTY_MINUTES_BEFORE
    ))

    private val eventChannel = Channel<AgendaDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                val restoredItemId = savedStateHandle.get<String>("itemId")
                if (restoredItemId != null) {
                    _state.update { it.copy(
                         taskyItem = it.taskyItem.copy(
                             id = restoredItemId
                         )
                    )}
                } else {
                    // If not available in SavedStateHandle, loading data from repository
                    getTaskyItemFromRepo(taskyType, taskyItemId)
                    savedStateHandle["itemId"] = taskyItemId

                }

                getTypeAndEditMode(isEdit, taskyType)

                hasLoadedInitialData = true
            }
        }
        .onEach { state ->
            savedStateHandle["titleText"] = state.titleText
            savedStateHandle["descriptionText"] = state.descriptionText
            savedStateHandle["isEditMode"] = state.isEditMode
            savedStateHandle["selectedDateMillis"] = state.selectedDateMillis
            savedStateHandle["notificationType"] = state.notificationType

        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    private suspend fun getTaskyItemFromRepo(taskyType: TaskyType, taskyItemId: String?) {
        if (taskyItemId == null) return

        val itemFromRepo = agendaRepository.getTaskyItem(taskyType, taskyItemId)
        _state.update { it.copy(
            taskyItem = itemFromRepo,
            titleText = itemFromRepo.title,
            descriptionText = itemFromRepo.description,
            time = itemFromRepo.time,
            selectedDateMillis = itemFromRepo.time.toEpochMilliseconds(),
            notificationType = itemFromRepo.notificationType
        ) }
    }


    private fun getTypeAndEditMode(editMode: Boolean, type: TaskyType){
        _state.update { it.copy(
            isEditMode = editMode,
            taskyItem = it.taskyItem.copy(
                type = type
            )
        ) }
    }

    private fun saveTaskyItem() {
        _state.update { it.copy(
            isSavingTaskyItem = true
        ) }

        viewModelScope.launch {
            val isTaskyItemIdBlank  = _state.value.taskyItem.id.isBlank()
            val newTaskyItemId = _state.value.taskyItem.id.ifBlank {
                randomUUID().toString()
            }
            val taskyItem = TaskyItem(
                title = _state.value.titleText ?: "",
                description = _state.value.descriptionText ?: "",
                time = _state.value.time,
                id = if (isTaskyItemIdBlank) newTaskyItemId else _state.value.taskyItem.id,
                type = _state.value.taskyItem.type,
                remindAt = _state.value.remindAt,
                details = _state.value.taskyItem.details,
                notificationType = _state.value.notificationType,
            )

            when(val result = if (isTaskyItemIdBlank) {
                agendaRepository.upsertTaskyItem(taskyItem)
            } else {
                agendaRepository.updateTaskyItem(taskyItem)
            }
            ) {
                is Result.Error -> {
                    eventChannel.send(AgendaDetailEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(AgendaDetailEvent.TaskyItemSaved)
                }
            }
        }
        _state.update { it.copy(
            isSavingTaskyItem = false
        ) }
    }

    private fun deleteTaskyItem() {
        _state.update { it.copy(
            isDeletingTaskyItem = true
        ) }

        viewModelScope.launch {
            when(val result = agendaRepository.deleteTaskyItem(
                taskyItemId = _state.value.taskyItem.id,
                taskyType = _state.value.taskyItem.type
            )) {
                is Result.Error -> {
                    eventChannel.send(AgendaDetailEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(AgendaDetailEvent.TaskyItemDeleted)
                }
            }
        }
        _state.update { it.copy(
            isDeletingTaskyItem = false
        ) }
    }



    fun onAction(action: AgendaDetailAction) {
        when (action) {

            AgendaDetailAction.OnEditModeClick -> {
                _state.update { it.copy(
                    isEditMode = true
                ) }
            }

            AgendaDetailAction.OnSaveClick -> {
                saveTaskyItem()
            }
            AgendaDetailAction.OnDeleteClick -> {
                deleteTaskyItem()
            }
            AgendaDetailAction.OnSetDateClick -> {
                _state.update { it.copy(
                    isDatePickerDialogVisible = true
                ) }
            }
            AgendaDetailAction.OnSetTimeClick -> {
                _state.update { it.copy(
                    isTimePickerDialogVisible = true
                )}
            }
            is AgendaDetailAction.ConfirmDateSelection -> {
                val currentSelectedTime = _state.value.time.time
                val newDate = action.selectedDateMillis.toLocalDateTime().date
                val newLocalDateTime = LocalDateTime(
                    year = newDate.year,
                    month = newDate.month,
                    day = newDate.day,
                    hour = currentSelectedTime.hour,
                    minute = currentSelectedTime.minute,
                    second = 0,
                    nanosecond = 0
                )
                _state.update { it.copy(
                    selectedDateMillis = newLocalDateTime.toEpochMilliseconds(),
                    time = newLocalDateTime,
                    isDatePickerDialogVisible = false
                ) }
            }

            AgendaDetailAction.OnDismissDatePickerDialog -> {
                _state.update { it.copy(
                    isDatePickerDialogVisible = false
                ) }
            }

            is AgendaDetailAction.ConfirmTimeSelection -> {
                val newHour = action.timePickerState.hour
                val newMinute = action.timePickerState.minute
                val newSecond = 0
                val currentDate = _state.value.time.date
                val newLocalDateTime = LocalDateTime(
                    year = currentDate.year,
                    month = currentDate.month,
                    day = currentDate.day,
                    hour = newHour,
                    minute = newMinute,
                    second = newSecond
                )
                _state.update { it.copy(
                    selectedDateMillis = newLocalDateTime.toEpochMilliseconds(),
                    time = newLocalDateTime,
                    isTimePickerDialogVisible = false
                ) }
            }

            AgendaDetailAction.OnDismissTimePickerDialog -> {
                _state.update { it.copy(
                    isTimePickerDialogVisible = false
                ) }
            }

            AgendaDetailAction.OnToggleNotificationDropdown -> {
                _state.update { it.copy(
                    isNotificationDropdownExpanded = !it.isNotificationDropdownExpanded
                ) }
            }

            is AgendaDetailAction.OnNotificationItemClick -> {
                val time = _state.value.time
                val timeInstant = time.toInstant(TimeZone.currentSystemDefault())

                _state.update {
                    it.copy(
                        notificationType = action.notificationType,
                        isNotificationDropdownExpanded = false
                    )
                }

                val remindAt = (timeInstant - action.notificationType.offset)
                    .toLocalDateTime(TimeZone.currentSystemDefault())

                _state.update {
                    it.copy(
                        remindAt = remindAt
                    )
                }
            }
            AgendaDetailAction.OnToggleDeleteBottomSheet -> {
                _state.update { it.copy(
                    isDeleteBottomSheetVisible = !it.isDeleteBottomSheetVisible
                ) }
            }

            is AgendaDetailAction.OnEditTextChanged -> {
                when(action.editTextType) {
                    EditTextType.TITLE -> {
                        _state.update { it.copy(
                            titleText = action.value
                        ) }
                    }
                    EditTextType.DESCRIPTION -> {
                        _state.update { it.copy(
                            descriptionText = action.value
                        ) }
                    }
                }
            }
            is AgendaDetailAction.OnAddPhoto -> {
                val photos = state.value.photos
                photos.add(action.photoUri)

                _state.update { it.copy(
                    photos = photos
                ) }
                println("PHOTOS State: ${state.value.photos}")

            }

            else -> Unit
        }
    }
}

/*
// Updating a shared property
state = state.copy(title = "new title")

// Updating an individual property
state = state.copy(
details = detailsAsEvent()?.copy(attendees = newAttendees)
)
*/