package com.jvoye.tasky.agenda.domain

import kotlinx.datetime.LocalDateTime

sealed interface AgendaItemDetails {
    data class Event(
        val toTime: LocalDateTime,
        val attendees: List<Attendee>,
        val photos: List<EventPhoto>
    ): AgendaItemDetails

    data class Task(
        val isDone: Boolean
    ): AgendaItemDetails

    data object Reminder: AgendaItemDetails
}

data class AgendaDetailState(
    val title: String = "",
    val description: String = "",
    val time: LocalDateTime? = null,
    val details: AgendaItemDetails? = null
)

/*
// Updating a shared property
state = state.copy(title = "new title")

// Updating an individual property
state = state.copy(
details = detailsAsEvent()?.copy(attendees = newAttendees)
)
*/


data class AgendaItem(
    val agendaItemId: Int,
    val agendaItemType: AgendaType,
    val agendaItemTitle: String,
    val agendaItemDescription: String,
    val agendaItemDate: String,
    val isAgendaItemFinished: Boolean,
)
