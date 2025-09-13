package com.jvoye.tasky.agenda.domain

import com.jvoye.tasky.core.domain.model.TaskyItem
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {
    fun observeTaskyItems(): Flow<List<TaskyItem>>
    suspend fun getTaskyItems(): List<TaskyItem>
    suspend fun getTaskyItem(taskyItemId: Long): TaskyItem
}