package com.jvoye.tasky.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jvoye.tasky.core.database.dao.TaskDao
import com.jvoye.tasky.core.database.dao.EventDao
import com.jvoye.tasky.core.database.dao.ReminderDao
import com.jvoye.tasky.core.database.entity.EventEntity
import com.jvoye.tasky.core.database.entity.ReminderEntity
import com.jvoye.tasky.core.database.entity.TaskEntity


@Database(
    entities = [
        TaskEntity::class,
        EventEntity::class,
        ReminderEntity::class
    ],
    version = 1
)
//@TypeConverters(RoomConverters::class)
abstract class TaskyDatabase: RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val eventDao: EventDao
    abstract val reminderDao: ReminderDao
}