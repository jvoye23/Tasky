package com.jvoye.tasky.agenda.presentation.agenda_list

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.TaskyItem
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
    data class OnAgendaTaskFinishedClick(val taskyItem: TaskyItem): AgendaAction
    data object OnToggleAgendaItemMoreMenu: AgendaAction
    data object OnToggleAgendaFabMenu: AgendaAction
    data class OnFabMenuItemClick(val isEditMode: Boolean, val taskyType: TaskyType): AgendaAction
    data class OnAgendaItemClick(val isEditMode: Boolean, val taskyType: TaskyType, val taskyItemId: String): AgendaAction
    data class OnAgendaItemMenuClick(val isEditMode: Boolean, val taskyType: TaskyType, val taskyItemId: String): AgendaAction
    data object OnDeleteClick: AgendaAction
    data object OnToggleDeleteBottomSheet: AgendaAction
    data class OnDeleteMenuItemClick(val taskyItem: TaskyItem): AgendaAction
    data object OnToggleDeleteDialog: AgendaAction
}