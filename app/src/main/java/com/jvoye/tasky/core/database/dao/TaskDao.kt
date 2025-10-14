package com.jvoye.tasky.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jvoye.tasky.core.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Upsert
    suspend fun upsertTasks(tasks: List<TaskEntity>)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: String): TaskEntity?

    @Query("SELECT * FROM tasks ORDER BY dateTimeUtc DESC")
    fun getTasks(): Flow<List<TaskEntity>>

    /**
     * Finds all entries for a specific LocalDate String like "2025-10-16"
     */
    @Query("SELECT * FROM tasks WHERE dateTimeUtc LIKE :localDateString ||'%'")
    fun getTasksForDate(localDateString: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}