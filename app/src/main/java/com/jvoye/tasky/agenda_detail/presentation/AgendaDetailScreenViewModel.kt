@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda_detail.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.domain.EditTextType
import com.jvoye.tasky.agenda_detail.domain.NotificationType
import com.jvoye.tasky.agenda_detail.presentation.mappers.toEpochMilliseconds
import com.jvoye.tasky.agenda_detail.presentation.mappers.toLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

class AgendaDetailScreenViewModel(
    private val isEdit: Boolean,
    private val taskyType: TaskyType,
    private val taskyItemId: Long?,
    private val agendaRepository: AgendaRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(AgendaDetailState(
        titleText = savedStateHandle["titleText"],
        descriptionText = savedStateHandle["descriptionText"],
        isEditMode = savedStateHandle.get<Boolean>("isEditMode") ?: false,
        selectedDateMillis = savedStateHandle.get<Long>("selectedDateMillis"),
        notificationType = savedStateHandle.get<NotificationType>("notificationType") ?: NotificationType.THIRTY_MINUTES_BEFORE
    ))
    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                val restoredItemId = savedStateHandle.get<Long>("itemId")
                if (restoredItemId != null) {
                    _state.update { it.copy(
                         taskyItem = it.taskyItem.copy(
                             id = restoredItemId
                         )
                    )}
                } else {
                    // If not available in SavedStateHandle, loading data from repository
                    getTaskyItemFromRepo(taskyItemId)
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

    private suspend fun getTaskyItemFromRepo(taskyItemId: Long?) {
        if (taskyItemId == null) return

        val itemFromRepo = agendaRepository.getTaskyItem(taskyItemId)
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

    fun onAction(action: AgendaDetailAction) {
        when (action) {

            AgendaDetailAction.OnEditModeClick -> {
                _state.update { it.copy(
                    isEditMode = true
                ) }
            }

            AgendaDetailAction.OnSaveClick -> {
                /*TODO()*/
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
                val currentDate = _state.value.time.date
                val newLocalDateTime = LocalDateTime(
                    year = currentDate.year,
                    month = currentDate.month,
                    day = currentDate.day,
                    hour = newHour,
                    minute = newMinute,
                    second = 0,
                    nanosecond = 0
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
                _state.update { it.copy(
                    notificationType = action.notificationType,
                    isNotificationDropdownExpanded = false
                ) }
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