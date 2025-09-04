package com.jvoye.tasky.agenda.domain

import com.jvoye.tasky.agenda.domain.AgendaType

data class AgendaItem(
    val agendaItemId: Int,
    val agendaItemType: AgendaType,
    val agendaItemTitle: String,
    val agendaItemDescription: String,
    val agendaItemDate: String,
    val isAgendaItemFinished: Boolean,
)
