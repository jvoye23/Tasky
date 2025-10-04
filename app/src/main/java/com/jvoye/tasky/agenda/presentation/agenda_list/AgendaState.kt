@file:OptIn(ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_list

import androidx.compose.material3.ExperimentalMaterial3Api
import com.jvoye.tasky.agenda.presentation.agenda_list.util.DateRowEntry
import com.jvoye.tasky.core.domain.model.TaskyItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// TODO(): Change dateHeadline string to UiText
@OptIn(ExperimentalMaterial3Api::class)
data class AgendaState(
    val selectedDateMillis: Long? = null,
    val isDatePickerDialogVisible: Boolean = false,
    val currentMonthName: String = "",
    val userInitials: String = "",
    val isLogoutDropdownVisible: Boolean = false,
    val isLoggingOut: Boolean = false,
    val dateRowEntries: List<DateRowEntry>? = null,
    val currentDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val dateHeadline: String = "Today",
    val agendaList: List<TaskyItem> = emptyList(),
    val isFabMenuExpanded: Boolean = false,
    val isAgendaItemMenuExpanded: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val isDeleteButtonLoading: Boolean = false,
    val taskyItemToBeDeleted: TaskyItem? = null
)
