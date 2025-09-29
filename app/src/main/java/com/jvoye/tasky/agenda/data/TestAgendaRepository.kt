package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.data.testTaskyItems
import com.jvoye.tasky.core.domain.TaskyItemId
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

class TestAgendaRepository : AgendaRepository {
    override fun getTaskyItems(): Flow<List<TaskyItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchFullAgenda(): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskyItem(
        taskyType: TaskyType,
        taskyItemId: TaskyItemId
    ): TaskyItem {
        TODO("Not yet implemented")
    }

    override suspend fun upsertTaskyItem(taskyItem: TaskyItem): EmptyResult<DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskyItem(
        taskyType: TaskyType,
        taskyItemId: TaskyItemId
    ) {
        TODO("Not yet implemented")
    }

}