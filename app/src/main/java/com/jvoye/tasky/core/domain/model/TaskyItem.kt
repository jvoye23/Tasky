package com.jvoye.tasky.core.domain.model

import android.os.Parcelable
import com.jvoye.tasky.agenda.domain.TaskyType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
@Parcelize
sealed interface TaskyItemDetails : Parcelable {
    @Serializable
    @Parcelize
    data class Event(
        val toTime: LocalDateTime,
        val attendees: List<Attendee>,
        val photos: List<EventPhoto>
    ): TaskyItemDetails, Parcelable

    data class Task(
        val isDone: Boolean
    ): TaskyItemDetails

    data object Reminder: TaskyItemDetails
}

@Serializable
@Parcelize
data class TaskyItem(
    val id: Long,
    val type: TaskyType,
    val title: String,
    val description: String,
    val time: LocalDateTime,
    val details: TaskyItemDetails
) : Parcelable