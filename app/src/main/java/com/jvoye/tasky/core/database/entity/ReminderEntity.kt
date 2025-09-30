package com.jvoye.tasky.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvoye.tasky.agenda.domain.TaskyType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "reminder")
data class ReminderEntity @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Uuid,
    val type: String,
    val title: String,
    val description: String,
    val dateTimeUtc: String,
    val remindAt: String
)