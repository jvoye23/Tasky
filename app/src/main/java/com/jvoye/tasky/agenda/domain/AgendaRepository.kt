package com.jvoye.tasky.agenda.domain

import kotlinx.coroutines.flow.Flow

interface AgendaRepository {
    suspend fun getAgendaItems(): List<AgendaItem>
    fun observeAgendaItems(): Flow<List<AgendaItem>>
}