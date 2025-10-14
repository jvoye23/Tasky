package com.jvoye.tasky.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvoye.tasky.core.domain.model.AttendeeBase
import com.jvoye.tasky.core.domain.model.EventAttendee
import com.jvoye.tasky.core.domain.model.RemotePhoto

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val dateTimeUtc: String,
    val toDateTimeUtc: String,
    val remindAtUtc: String? = null,
    val attendees: List<EventAttendee>,
    val remotePhotos: List<RemotePhoto>,
    val isUserEventCreator: Boolean,
    val host: String
)
