package com.jvoye.tasky.agenda.presentation.agenda_details

import androidx.compose.foundation.text.input.TextFieldState
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.presentation.agenda_details.components.AttendeeFilterType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.getNextHalfMarkLocalTime
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toEpochMilliseconds
import com.jvoye.tasky.core.domain.model.AttendeeBase
import com.jvoye.tasky.core.domain.model.EventAttendee
import com.jvoye.tasky.core.domain.model.RemotePhoto
import com.jvoye.tasky.core.domain.model.LocalPhotoInfo
import com.jvoye.tasky.core.domain.model.LookupAttendee
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
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
    val selectedToDateMillis: Long = selectedDateMillis + 30.minutes.inWholeMilliseconds,
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES_BEFORE,

    val isDatePickerDialogVisible: Boolean = false,
    val isTimePickerDialogVisible: Boolean = false,
    val isToDatePickerDialogVisible: Boolean = false,
    val isToTimePickerDialogVisible: Boolean = false,
    val isNotificationDropdownExpanded: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val isDeleteButtonLoading: Boolean = false,

    val isSavingTaskyItem: Boolean = false,
    val isDeletingTaskyItem: Boolean = false,
    val remotePhotos: List<String> = emptyList<String>(),
    val remotePhotoInfos: List<RemotePhoto> = emptyList(),
    val isOnline: Boolean = true,

    val host: String? = null,
    val isUserEventCreator: Boolean = true,
    val fromTime: LocalDateTime = getNextHalfMarkLocalTime(),
    val toTime: LocalDateTime = (getNextHalfMarkLocalTime().toInstant(TimeZone.currentSystemDefault()) + 30.minutes).toLocalDateTime(TimeZone.currentSystemDefault()),
    val isFromTimeExpanded: Boolean = false,
    val isToTimeExpanded: Boolean = false,
    val eventAttendees: List<EventAttendee> = emptyList<EventAttendee>(),
    val lookupAttendees: List<AttendeeBase> = emptyList<AttendeeBase>(),
    val deletedPhotoKeys: List<String> = emptyList<String>(),
    val eventPhotos: List<RemotePhoto> = emptyList<RemotePhoto>(),

    val isAddAttendeeBottomSheetVisible: Boolean = false,
    val attendeeFilter: AttendeeFilterType = AttendeeFilterType.ALL,
    val isAddAttendeeButtonLoading: Boolean = false,
    val emailInput: TextFieldState = TextFieldState(),
    val emailErrorText: UiText? = null,
    val doesEmailExist: Boolean = false,
    val validEmail: String = "",
    val invitedAttendee: LookupAttendee? = null,
    val isDone: Boolean = false,

    val newLocalPhotoInfos: List<LocalPhotoInfo> = emptyList<LocalPhotoInfo>(),

    val photoGridItems: List<PhotoGridItem> = emptyList<PhotoGridItem>()

)

data class PhotoGridItem(
    val remoteKey: String?,
    val localPhotoKey: String?,
    val url: String?,
    val localPath: String?
)

fun LocalPhotoInfo.toPhotoGridItem() = PhotoGridItem(
    remoteKey = null,
    url = null,
    localPhotoKey = localPhotoKey,
    localPath = filePath
)

fun RemotePhoto.toPhotoGridItem() = PhotoGridItem(
    remoteKey = key,
    url = url,
    localPhotoKey = null,
    localPath = null,
)