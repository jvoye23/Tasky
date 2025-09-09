package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AgendaItem
import com.jvoye.tasky.agenda.domain.AgendaType

val testAgendaItems = mutableListOf<AgendaItem>(
    AgendaItem(
        agendaItemId = 1,
        agendaItemType = AgendaType.TASK,
        agendaItemTitle = "Task 1",
        agendaItemDescription = "Description for Task 1",
        agendaItemDate = "Mar 07, 11:59",
        isAgendaItemFinished = true
    ),
    AgendaItem(
        agendaItemId = 2,
        agendaItemType = AgendaType.EVENT,
        agendaItemTitle = "Event 1",
        agendaItemDescription = "Description for Event 1",
        agendaItemDate = "Mar 5, 10:00 - Mar 07, 11:59",
        isAgendaItemFinished = true
    ),
    AgendaItem(
        agendaItemId = 3,
        agendaItemType = AgendaType.REMINDER,
        agendaItemTitle = "Reminder 1",
        agendaItemDescription = "Description for Reminder 1",
        agendaItemDate = "Mar 07, 11:59",
        isAgendaItemFinished = false
    ),
    AgendaItem(
        agendaItemId = 4,
        agendaItemType = AgendaType.EVENT,
        agendaItemTitle = "Event 5",
        agendaItemDescription = "Description for Event 5",
        agendaItemDate = "Mar 5, 10:00 - Mar 07, 11:59",
        isAgendaItemFinished = true
    ),
    AgendaItem(
        agendaItemId = 5,
        agendaItemType = AgendaType.EVENT,
        agendaItemTitle = "Event 2",
        agendaItemDescription = "Description for Event 2",
        agendaItemDate = "Mar 5, 10:00 - Mar 07, 11:59",
        isAgendaItemFinished = false
    ),
    AgendaItem(
        agendaItemId = 6,
        agendaItemType = AgendaType.REMINDER,
        agendaItemTitle = "Reminder 1",
        agendaItemDescription = "Description for Reminder 1",
        agendaItemDate = "Mar 07, 11:59",
        isAgendaItemFinished = true
    ),
    AgendaItem(
        agendaItemId = 7,
        agendaItemType = AgendaType.REMINDER,
        agendaItemTitle = "Reminder 2",
        agendaItemDescription = "Description for Reminder 2",
        agendaItemDate = "Mar 07, 11:59",
        isAgendaItemFinished = false
    ),
    AgendaItem(
        agendaItemId = 8,
        agendaItemType = AgendaType.TASK,
        agendaItemTitle = "Task 4",
        agendaItemDescription = "Description for Task 4",
        agendaItemDate = "Mar 07, 11:59",
        isAgendaItemFinished = true
    ),
    AgendaItem(
        agendaItemId = 9,
        agendaItemType = AgendaType.EVENT,
        agendaItemTitle = "Event 3",
        agendaItemDescription = "Description for Event 3",
        agendaItemDate = "Mar 5, 10:00 - Mar 07, 11:59",
        isAgendaItemFinished = false
    )
)