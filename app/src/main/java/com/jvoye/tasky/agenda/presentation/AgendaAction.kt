package com.jvoye.tasky.agenda.presentation

sealed interface AgendaAction {
    data object OnUserInitialsClick: AgendaAction
    data object OnCalendarIconClick: AgendaAction
    data object OnMonthTextClick: AgendaAction
    data class ConfirmDateSelection(val selectedDateMillis: Long): AgendaAction
    data object OnDismissDatePickerDialog: AgendaAction
    data object OnDismissAgendaLogoutDropdown: AgendaAction
    data object OnLogOutClick: AgendaAction
}