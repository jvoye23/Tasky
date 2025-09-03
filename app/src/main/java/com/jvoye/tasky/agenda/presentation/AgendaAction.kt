package com.jvoye.tasky.agenda.presentation

import kotlinx.datetime.LocalDate

sealed interface AgendaAction {
    data object OnUserInitialsClick: AgendaAction
    data object OnCalendarIconClick: AgendaAction
    data object OnMonthTextClick: AgendaAction
    data class ConfirmDateSelection(val selectedDateMillis: Long): AgendaAction
    data object OnDismissDatePickerDialog: AgendaAction
    data object OnDismissAgendaLogoutDropdown: AgendaAction
    data object OnLogOutClick: AgendaAction
    data class OnDateRowItemClick(val selectedDate: LocalDate): AgendaAction
    data class OnAgendaTaskFinishedClick(val agendaItemId: Int): AgendaAction
    data object OnItemMoreClick: AgendaAction
}