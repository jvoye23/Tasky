package com.jvoye.tasky.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val dateTimeUtc: String,
    val toDateTimeUtc: String,
    val remindAtUtc: String,
    val attendees: String,
    val photos: String,
    val isUserEventCreator: Boolean,
    val host: String
)
