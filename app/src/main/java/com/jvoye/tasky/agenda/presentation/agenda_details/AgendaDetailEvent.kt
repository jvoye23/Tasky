package com.jvoye.tasky.agenda.presentation.agenda_details

import com.jvoye.tasky.agenda.presentation.agenda_list.AgendaEvent
import com.jvoye.tasky.core.presentation.designsystem.util.UiText

sealed interface AgendaDetailEvent {

    data object TaskyItemSaved: AgendaDetailEvent
    data object TaskyItemDeleted: AgendaDetailEvent
    data class Error(val error: UiText): AgendaDetailEvent
}