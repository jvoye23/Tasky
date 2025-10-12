@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda.presentation.agenda_details

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.jvoye.tasky.agenda.domain.EditTextType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AttendeeFilterType
import com.jvoye.tasky.core.domain.model.AttendeeBase

sealed interface AgendaDetailAction {
    data object OnEditModeClick : AgendaDetailAction
    data object OnSaveClick : AgendaDetailAction
    data object OnDeleteClick : AgendaDetailAction
    data object OnCloseAndCancelClick : AgendaDetailAction
    data class OnEditTextClick(val text: String?, val editTextType: EditTextType) : AgendaDetailAction
    data class ConfirmDateSelection(val selectedDateMillis: Long) : AgendaDetailAction
    data class ConfirmTimeSelection(val timePickerState: TimePickerState) : AgendaDetailAction
    data class ConfirmToDateSelection(val toDateSelectedDateMillis: Long): AgendaDetailAction
    data class ConfirmToTimeSelection(val toTimePickerState: TimePickerState) : AgendaDetailAction
    data object OnToggleNotificationDropdown : AgendaDetailAction
    data class OnNotificationItemClick(val notificationType: NotificationType) : AgendaDetailAction
    data object OnToggleDeleteBottomSheet : AgendaDetailAction
    data class OnEditTextChanged(val editTextType: EditTextType, val value: String): AgendaDetailAction
    data class OnAddLocalPhotos(val photos: List<Uri>): AgendaDetailAction
    data class OnPhotoClick(val localPhotoPath: String?, val photoUrl: String?): AgendaDetailAction
    data class OnDeleteAttendee(val attendeeBase: AttendeeBase): AgendaDetailAction
    data class OnAddAttendee(val email: String): AgendaDetailAction
    data object OnToggleAddAttendeeBottomSheet : AgendaDetailAction
    data class OnChangeAttendeeFilter(val attendeeFilter: AttendeeFilterType): AgendaDetailAction

    data object OnToggleTimePickerDialog: AgendaDetailAction
    data object OnToggleDatePickerDialog: AgendaDetailAction
    data object OnToggleToTimePickerDialog: AgendaDetailAction
    data object OnToggleToDatePickerDialog: AgendaDetailAction
}