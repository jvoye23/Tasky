package com.jvoye.tasky.agenda.presentation

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
data class AgendaState(
    val selectedDateMillis: Long? = null,
    val isDatePickerDialogVisible: Boolean = false,
    val currentMonthName: String = "",
    val userInitials: String = "",
    val isLogoutDropdownVisible: Boolean = false,
    val isLoggingOut: Boolean = false
)
