package com.jvoye.tasky.core.domain.model

import com.jvoye.tasky.agenda.domain.TaskyType
import kotlinx.datetime.LocalDateTime

sealed interface TaskyItemDetails {
    data class Event(
        val toTime: LocalDateTime,
        val attendees: List<Attendee>,
        val photos: List<EventPhoto>
    ): TaskyItemDetails

    data class Task(
        val isDone: Boolean
    ): TaskyItemDetails

    data object Reminder: TaskyItemDetails
}

data class TaskyItem(
    val id: Long,
    val type: TaskyType,
    val title: String,
    val description: String,
    val time: LocalDateTime,
    val details: TaskyItemDetails
)