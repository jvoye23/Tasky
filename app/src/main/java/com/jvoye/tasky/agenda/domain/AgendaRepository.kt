package com.jvoye.tasky.agenda.domain

import com.jvoye.tasky.core.domain.TaskyItemId
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface AgendaRepository {

    fun getTaskyItems(): Flow<List<TaskyItem>>
    suspend fun fetchFullAgenda(): EmptyResult<DataError>
    suspend fun getTaskyItem(taskyType: TaskyType, taskyItemId: TaskyItemId) : TaskyItem

    suspend fun upsertTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError>

    suspend fun deleteTaskyItem(taskyType: TaskyType, taskyItemId: TaskyItemId)

}