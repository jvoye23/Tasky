package com.jvoye.tasky.core.domain.model

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

sealed interface TaskyItemDetails {
    @OptIn(ExperimentalTime::class)
    data class Event(
        val toTime: Instant,
        val eventAttendees: List<EventAttendee>,
        val lookupAttendees: List<AttendeeBase> = emptyList(),
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

@OptIn(ExperimentalTime::class)
data class TaskyItem(
    val id: String,
    val type: TaskyType,
    val title: String,
    val description: String,
    val time: Instant,
    val remindAt: Instant? = null,
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