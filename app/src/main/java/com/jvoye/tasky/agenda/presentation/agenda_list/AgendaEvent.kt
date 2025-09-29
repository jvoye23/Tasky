package com.jvoye.tasky.agenda.presentation.agenda_list

import com.jvoye.tasky.core.presentation.designsystem.util.UiText

sealed interface AgendaEvent {
    data object LogoutSuccess: AgendaEvent
    data class Error(val error: UiText): AgendaEvent
}