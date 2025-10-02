package com.jvoye.tasky.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jvoye.tasky.core.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEvent(id: String)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()



}