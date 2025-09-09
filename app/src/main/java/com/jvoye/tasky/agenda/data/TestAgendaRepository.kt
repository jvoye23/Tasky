package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AgendaItem
import com.jvoye.tasky.agenda.domain.AgendaRepository
import kotlinx.coroutines.flow.Flow

class TestAgendaRepository : AgendaRepository {

    override suspend fun getAgendaItems(): List<AgendaItem> {
        return testAgendaItems
    }

    override fun observeAgendaItems(): Flow<List<AgendaItem>> {
        TODO("Not yet implemented")
    }
}