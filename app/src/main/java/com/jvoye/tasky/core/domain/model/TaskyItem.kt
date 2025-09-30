package com.jvoye.tasky.core.domain.model

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import kotlinx.datetime.LocalDateTime

sealed interface TaskyItemDetails {
    data class Event(
        val toTime: LocalDateTime,
        val attendees: List<Attendee>,
        val photos: List<EventPhoto>,
        val isUserEventCreator: Boolean,
        val host: String
    ): TaskyItemDetails

    data class Task(
        val isDone: Boolean
    ): TaskyItemDetails

    data object Reminder: TaskyItemDetails
}

data class TaskyItem(
    val id: String,
    val type: TaskyType,
    val title: String,
    val description: String,
    val time: LocalDateTime,
    val remindAt: LocalDateTime,
    val details: TaskyItemDetails,
    val notificationType: NotificationType = NotificationType.THIRTY_MINUTES_BEFORE
)