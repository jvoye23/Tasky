package com.jvoye.tasky.core.domain

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

typealias TaskyItemId = String

interface LocalTaskyItemDataSource {
    fun getTaskyItems(): Flow<List<TaskyItem>>
    suspend fun getTaskyItem(taskyType: TaskyType, taskyItemId: String): TaskyItem
    suspend fun upsertTaskyItem(taskyItem: TaskyItem): Result<TaskyItemId, DataError.Local>
    suspend fun upsertFullAgenda(fullAgenda: FullAgenda): Result<List<TaskyItemId>, DataError.Local>
    suspend fun deleteTaskyItem(type: TaskyType, taskyItemId: String)
    suspend fun deleteAllTaskyItems()
}

