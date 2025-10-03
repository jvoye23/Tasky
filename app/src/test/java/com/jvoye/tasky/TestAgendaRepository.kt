package com.jvoye.tasky

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.data.testTaskyItems
import com.jvoye.tasky.core.domain.TaskyItemId
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestAgendaRepository : MockAgendaRepository {
    override fun getTaskyItems(): Flow<List<TaskyItem>> {
        return flowOf(testTaskyItems)
    }

    override suspend fun fetchFullAgenda(): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun getTaskyItem(
        taskyType: TaskyType,
        taskyItemId: TaskyItemId
    ): TaskyItem {
        return testTaskyItems.first() { it.id == taskyItemId }
    }

    override suspend fun upsertTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun deleteTaskyItem(
        taskyType: TaskyType,
        taskyItemId: TaskyItemId
    ) {
        testTaskyItems.removeIf { it.id == taskyItemId }
    }

}