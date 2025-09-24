@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda_detail.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.jvoye.tasky.agenda_detail.domain.EditTextType
import com.jvoye.tasky.agenda_detail.domain.NotificationType

sealed interface AgendaDetailAction {
    data object OnEditModeClick : AgendaDetailAction
    data object OnSaveClick : AgendaDetailAction
    data object OnCloseAndCancelClick : AgendaDetailAction
    data class OnEditTextClick(val text: String, val editTextType: EditTextType) : AgendaDetailAction
    data object OnSetTimeClick : AgendaDetailAction
    data object OnSetDateClick : AgendaDetailAction
    data object OnNotificationTimerClick : AgendaDetailAction
    data object OnDismissDatePickerDialog : AgendaDetailAction
    data class ConfirmDateSelection(val selectedDateMillis: Long) : AgendaDetailAction
    data class ConfirmTimeSelection(val timePickerState: TimePickerState) : AgendaDetailAction
    data object OnDismissTimePickerDialog : AgendaDetailAction
    data object OnToggleNotificationDropdown : AgendaDetailAction
    data class OnNotificationItemClick(val notificationType: NotificationType) : AgendaDetailAction
}