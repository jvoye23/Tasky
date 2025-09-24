@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda_detail.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.domain.EditTextType
import com.jvoye.tasky.core.domain.model.TaskyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class AgendaDetailScreenViewModel(
    private val isEdit: Boolean,
    private val taskyType: TaskyType,
    private val taskyItemId: Long?,
    private val agendaRepository: AgendaRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(AgendaDetailState(

    ))
    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                // Try to restore TaskyItem from SavedStateHandle first
                val restoredTaskyItem = savedStateHandle.get<TaskyItem>(TASKY_ITEM_KEY)

                if (restoredTaskyItem != null) {
                    _state.update { it.copy(
                        taskyItem = restoredTaskyItem
                    )}
                } else {
                    // If not available in SavedStateHandle, loading from repository
                    getTaskyItemFromRepo(taskyItemId)
                }
                getTypeAndEditMode(isEdit, taskyType)
                hasLoadedInitialData = true
            }
        }
        .onEach {
            savedStateHandle[TASKY_ITEM_KEY] = it.taskyItem
            println("TASKY ITEM SAVED STATE: ${it.taskyItem}")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    fun updateEditedText(editedText: String, editTextType: EditTextType) {
        when(editTextType){
            EditTextType.TITLE -> {
                _state.update { it.copy(
                    taskyItem = it.taskyItem.copy(
                        title = editedText
                    )
                ) }
            }
            EditTextType.DESCRIPTION -> {
                _state.update { it.copy(
                    taskyItem = it.taskyItem.copy(
                        description = editedText
                    )
                ) }
            }
        }
    }


    private suspend fun getTaskyItemFromRepo(taskyItemId: Long?) {
        if (taskyItemId == null) return

        val itemFromRepo = agendaRepository.getTaskyItem(taskyItemId)
        _state.update { it.copy(
            taskyItem = itemFromRepo
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

            AgendaDetailAction.OnNotificationTimerClick -> {
                /*TODO()*/
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

                val dateMillis = action.selectedDateMillis
                val dateInstant = Instant.fromEpochMilliseconds(dateMillis)
                val newLocalDate = dateInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date

                _state.update { it.copy(
                    taskyItem = it.taskyItem.copy(
                        time = newLocalDate.atTime(it.taskyItem.time.hour, it.taskyItem.time.minute)),
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
                val currentDate = _state.value.taskyItem.time.date
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
                    taskyItem = it.taskyItem.copy(
                        time = newLocalDateTime
                    ),
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

            else -> Unit
        }
    }
    companion object {
        private const val TASKY_ITEM_KEY = "taskyItemState"
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