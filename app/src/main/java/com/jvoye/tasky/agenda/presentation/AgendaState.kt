package com.jvoye.tasky.agenda.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import com.jvoye.tasky.agenda.presentation.util.DateRowEntry
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
data class AgendaState(
    val selectedDateMillis: Long? = null,
    val isDatePickerDialogVisible: Boolean = false,
    val currentMonthName: String = "",
    val userInitials: String = "",
    val isLogoutDropdownVisible: Boolean = false,
    val isLoggingOut: Boolean = false,
    val dateRowEntries: List<DateRowEntry>? = null,
    val currentDate: LocalDate? = null,
    val dateHeadline: String = "Date Headline"
)
