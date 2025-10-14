package com.jvoye.tasky.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jvoye.tasky.core.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface EventDao {
    @Upsert
    suspend fun upsertEvent(event: EventEntity)

    @Upsert
    suspend fun upsertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEvent(id: String): EventEntity?

    @Query("SELECT * FROM events ORDER BY dateTimeUtc DESC")
    fun getEvents(): Flow<List<EventEntity>>

    /**
     * Finds all entries for a specific LocalDate String like "2025-10-16"
     */
    @Query("SELECT * FROM events WHERE dateTimeUtc LIKE :localDateString ||'%'")
    fun getEventsForDate(localDateString: String): Flow<List<EventEntity>>

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: String)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()



}