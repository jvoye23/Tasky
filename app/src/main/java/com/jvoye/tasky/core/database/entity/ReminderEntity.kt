package com.jvoye.tasky.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val dateTimeUtc: String,
    val remindAtUtc: String
)