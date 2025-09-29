package com.jvoye.tasky.agenda.presentation.agenda_details

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// TODO(): Change strings to UiText
data class AgendaDetailState @OptIn(ExperimentalTime::class) constructor(
    val taskyItem: TaskyItem = TaskyItem(
        title = "Title",
        description = "Description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        id = "0",
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(isDone = false),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    val titleText: String? = null,
    val descriptionText: String? = null,
    val time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isEditMode: Boolean = false,
    val selectedDateMillis: Long? = null,
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES_BEFORE,

    val isDatePickerDialogVisible: Boolean = false,
    val isTimePickerDialogVisible: Boolean = false,
    val isNotificationDropdownExpanded: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val isDeleteButtonLoading: Boolean = false,

    val isSavingTaskyItem: Boolean = false
)