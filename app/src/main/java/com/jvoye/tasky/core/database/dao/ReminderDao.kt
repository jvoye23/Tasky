package com.jvoye.tasky.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jvoye.tasky.core.database.entity.ReminderEntity

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface ReminderDao {
    @Upsert
    suspend fun upsertReminder(reminder: ReminderEntity)

    @Upsert
    suspend fun upsertReminders(reminders: List<ReminderEntity>)

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminder(id: String): ReminderEntity?

    @Query("SELECT * FROM reminders ORDER BY dateTimeUtc DESC")
    fun getReminders(): Flow<List<ReminderEntity>>

    /**
     * Finds all entries for a specific LocalDate String like "2025-10-16"
     */
    @Query("SELECT * FROM reminders WHERE dateTimeUtc LIKE :localDateString ||'%'")
    fun getRemindersForDate(localDateString: String): Flow<List<ReminderEntity>>

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminder(id: String)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()
}