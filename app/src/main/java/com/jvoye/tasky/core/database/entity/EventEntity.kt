package com.jvoye.tasky.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.Attendee
import com.jvoye.tasky.core.domain.model.EventPhoto
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "events")
data class EventEntity
@OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Uuid,
    val type: String,
    val title: String,
    val description: String,
    val dateTimeUtc: String,
    val toDateTimeUtc: String,
    val remindAt: String,
    val attendees: List<Attendee>,
    val photos: List<EventPhoto>,
    val isUserEventCreator: Boolean,
    val host: String
)
