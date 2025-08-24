@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.core.data.auth.EncryptedSessionDataStore
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.presentation.mappers.toUserUi
import com.jvoye.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AgendaViewModel(
    private val encryptedSessionDataStore: EncryptedSessionDataStore,
    private val authRepository: AuthRepository
): ViewModel() {

    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(AgendaState())

    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getCurrentMonthName()
                getUserInitials()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            _state.value
        )

    fun onAction(action: AgendaAction){
        when(action){
            AgendaAction.OnUserInitialsClick -> {
                _state.update { it.copy(
                    isLogoutDropdownVisible = true
                ) }
            }

            AgendaAction.OnCalendarIconClick,
            AgendaAction.OnMonthTextClick -> {
                _state.update { it.copy(
                    isDatePickerDialogVisible = true
                ) }
            }

            is AgendaAction.ConfirmDateSelection -> {
                _state.update { it.copy(
                    selectedDateMillis = action.selectedDateMillis)
                }
            }

            AgendaAction.OnDismissDatePickerDialog -> {
                _state.update { it.copy(
                    isDatePickerDialogVisible = false
                ) }
            }

            AgendaAction.OnDismissAgendaLogoutDropdown -> {
                _state.update { it.copy(
                    isLogoutDropdownVisible = false
                ) }
            }

            AgendaAction.OnLogOutClick -> onLogoutClick()
        }
    }

    private fun getCurrentMonthName() {
        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month: Month = localDateTime.month

        _state.update { it.copy(
            currentMonthName = month.name
        ) }
    }

    private suspend fun getUserInitials() {
        val userInitials = encryptedSessionDataStore.get()?.toUserUi()?.userInitials
        _state.update { it.copy(
            userInitials = userInitials.toString()
        ) }

    }

    private fun onLogoutClick() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLogoutDropdownVisible = false,
                isLoggingOut = true,
            ) }

            val result = authRepository.logout()

            _state.update { it.copy(
                isLoggingOut = false,
            ) }

            when(result) {
                is Result.Error -> {
                    eventChannel.send(AgendaEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(AgendaEvent.LogoutSuccess)
                }
            }
        }
    }
}



