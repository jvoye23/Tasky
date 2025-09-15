package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.core.data.testTaskyItems
import com.jvoye.tasky.core.domain.model.TaskyItem
import kotlinx.coroutines.flow.Flow

class TestAgendaRepository : AgendaRepository {

    override fun observeTaskyItems(): Flow<List<TaskyItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskyItems(): List<TaskyItem> {
        return testTaskyItems
    }

    override suspend fun getTaskyItem(taskyItemId: Long): TaskyItem {
        return testTaskyItems.first() { it.id == taskyItemId }
    }
}