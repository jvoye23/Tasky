package com.jvoye.tasky.agenda.presentation.agenda_details

import androidx.compose.foundation.text.input.TextFieldState
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AttendeeFilterType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.getNextHalfMarkLocalTime
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toEpochMilliseconds
import com.jvoye.tasky.core.domain.model.Attendee
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.domain.model.User
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

// TODO(): Change strings to UiText
data class AgendaDetailState @OptIn(ExperimentalTime::class) constructor(
    val taskyItem: TaskyItem = TaskyItem(
        title = "Title",
        description = "Description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        id = "",
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(isDone = false),
        remindAt = (Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()) - 30.minutes).toLocalDateTime(TimeZone.currentSystemDefault()),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    val titleText: String? = null,
    val descriptionText: String? = null,
    val time: LocalDateTime = getNextHalfMarkLocalTime(),
    val remindAt: LocalDateTime = (time.toInstant(TimeZone.currentSystemDefault()) - 30.minutes).toLocalDateTime(TimeZone.currentSystemDefault()),
    val isEditMode: Boolean = false,
    val selectedDateMillis: Long = getNextHalfMarkLocalTime().toEpochMilliseconds(),
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES_BEFORE,

    val isDatePickerDialogVisible: Boolean = false,
    val isTimePickerDialogVisible: Boolean = false,
    val isNotificationDropdownExpanded: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val isDeleteButtonLoading: Boolean = false,

    val isSavingTaskyItem: Boolean = false,
    val isDeletingTaskyItem: Boolean = false,
    val localPhotos: List<String> = emptyList<String>(),
    val remotePhotos: List<String> = emptyList<String>(),
    val isOnline: Boolean = true,

    val host: String? = null,
    val isUserEventCreator: Boolean = false,
    val fromTime: LocalDateTime = getNextHalfMarkLocalTime(),
    val toTime: LocalDateTime = getNextHalfMarkLocalTime(),
    val attendees: List<Attendee> = emptyList<Attendee>(),
    val photoKeys: List<String> = emptyList<String>(),

    val isAddAttendeeBottomSheetVisible: Boolean = false,
    val attendeeFilter: AttendeeFilterType = AttendeeFilterType.ALL,
    val isAddAttendeeButtonLoading: Boolean = false,
    val emailInput: TextFieldState = TextFieldState(),
    val emailErrorText: UiText.StringResource? = null,
    val doesEmailExist: Boolean = false,
    val validEmail: String = "",
    val invitedUser: User? = null

)