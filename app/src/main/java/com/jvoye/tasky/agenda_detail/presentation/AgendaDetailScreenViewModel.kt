@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda_detail.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    private val agendaRepository: AgendaRepository
): ViewModel() {

    private val _state = MutableStateFlow(AgendaDetailState(
    ))
    private var hasLoadedInitialData = false





    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getTypeAndEditMode(isEdit, taskyType)
                getTaskyItem(taskyItemId)
                hasLoadedInitialData = true
            }
            println("CURRENT STATE: ${_state.value}")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    private suspend fun getTaskyItem(taskyItemId: Long?) {
        if (taskyItemId == null) return
        _state.update { it.copy(
            taskyItem = agendaRepository.getTaskyItem(taskyItemId)
        ) }
        println("CURRENT STATE: ${_state.value}")
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
            AgendaDetailAction.OnEditDescriptionClick -> {
                /*TODO()*/
            }
            AgendaDetailAction.OnEditModeClick -> {
                _state.update { it.copy(
                    isEditMode = true
                ) }
            }
            AgendaDetailAction.OnEditTitleClick -> {
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

}

/*
// Updating a shared property
state = state.copy(title = "new title")

// Updating an individual property
state = state.copy(
details = detailsAsEvent()?.copy(attendees = newAttendees)
)
*/