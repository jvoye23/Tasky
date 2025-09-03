package com.jvoye.tasky.agenda.domain

interface AgendaRepository {
    suspend fun getAgendaItems(): List<AgendaItem>
}