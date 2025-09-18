package com.jvoye.tasky.agenda_detail.presentation

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.domain.NotificationType
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// TODO(): Change strings to UiText
data class AgendaDetailState @OptIn(ExperimentalTime::class) constructor(
    val taskyItem: TaskyItem = TaskyItem(
        title = "Title",
        description = "Description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        id = 0L,
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(isDone = false)
    ),
    val isEditMode: Boolean = false,
    val isDatePickerDialogVisible: Boolean = false,
    val isTimePickerDialogVisible: Boolean = false,
    val selectedDateMillis: Long? = null,
    val taskyItemDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES_BEFORE,
    val isNotificationDropdownExpanded: Boolean = false
)