@file:OptIn(ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.AgendaItem
import com.jvoye.tasky.agenda.presentation.util.DateRowEntry
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
    val agendaList: List<AgendaItem>? = null
    )
