package com.jvoye.tasky.core.domain.model

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import kotlinx.datetime.LocalDateTime

sealed interface TaskyItemDetails {
    data class Event(
        val toTime: LocalDateTime,
        val eventAttendees: List<EventAttendee>,
        val photos: List<LocalPhotoInfo>,
        val newPhotosKeys: List<String> = emptyList(),
        val deletedPhotoKeys: List<String> = emptyList(),
        val remotePhotos: List<RemotePhoto> = emptyList(),
        val isUserEventCreator: Boolean,
        val host: String,
        val isGoing: Boolean = true
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

fun TaskyItem.detailsAsEvent(): TaskyItemDetails.Event? {
    return details as? TaskyItemDetails.Event
}

fun TaskyItem.detailsAsTask(): TaskyItemDetails.Task? {
    return details as? TaskyItemDetails.Task
}

fun TaskyItem.detailsAsReminder(): TaskyItemDetails.Reminder? {
    return details as? TaskyItemDetails.Reminder
}