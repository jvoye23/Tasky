package com.jvoye.tasky.agenda.presentation.agenda_details

import android.net.Uri
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.getNextHalfMarkLocalTime
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toEpochMilliseconds
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.util.UUID.randomUUID
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
    val remotePhotos: List<String> = emptyList<String>()
)