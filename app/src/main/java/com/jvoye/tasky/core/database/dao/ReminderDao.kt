package com.jvoye.tasky.core.database.dao

import androidx.room.Query
import androidx.room.Upsert
import com.jvoye.tasky.core.database.entity.ReminderEntity

import kotlinx.coroutines.flow.Flow

interface ReminderDao {
    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Upsert
    suspend fun upsertReminders(reminders: List<ReminderEntity>)

    @Query("SELECT * FROM reminder WHERE id = :id")
    suspend fun getReminder(id: String): ReminderEntity

    @Query("SELECT * FROM reminder ORDER BY dateTimeUtc DESC")
    fun getReminders(): Flow<List<ReminderEntity>>

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun deleteReminder(id: String)

    @Query("DELETE FROM reminder")
    suspend fun deleteAllReminders()
}