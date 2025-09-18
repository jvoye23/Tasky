@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.presentation.util.DateRowEntry
import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.core.data.auth.EncryptedSessionDataStore
import com.jvoye.tasky.core.domain.model.TaskyNavParcelable
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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AgendaViewModel(
    private val encryptedSessionDataStore: EncryptedSessionDataStore,
    private val authRepository: AuthRepository,
    private val agendaRepository: AgendaRepository
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
                getDateRowEntries()
                getAgendaListTitle()
                getAgendaItems()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
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
                    selectedDateMillis = action.selectedDateMillis,
                    isDatePickerDialogVisible = false)
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

            is AgendaAction.OnDateRowItemClick -> {
                _state.update { it.copy(
                    currentDate = action.selectedDate
                ) }
                getAgendaListTitle()
            }

            AgendaAction.OnToggleAgendaItemMoreMenu -> {
                _state.update { it.copy(
                    isAgendaItemMenuExpanded = it.isAgendaItemMenuExpanded.not()
                ) }
            }
            is AgendaAction.OnAgendaTaskFinishedClick -> {

            }

            AgendaAction.OnToggleAgendaFabMenu -> {
                _state.update {
                    it.copy(
                        isFabMenuExpanded = it.isFabMenuExpanded.not()
                    )
                }
            }
            else -> Unit
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

    private fun getAgendaListTitle() {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val currentDate = state.value.currentDate
        if (currentDate == today) {
            _state.update { it.copy(
                dateHeadline = "Today"
            ) }
        } else {
            _state.update { it.copy(
                dateHeadline = currentDate.toString()
            ) }
        }
    }

    private fun getAgendaItems() {
        viewModelScope.launch {
            _state.update { it.copy(
                agendaList = agendaRepository.getTaskyItems()
            ) }
        }
    }


    private fun getDateRowEntries() {
        val entries = mutableListOf<DateRowEntry>()
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val fifteenDaysAgo = today.minus(DatePeriod(days = 15))
        val fifteenDaysAhead = today.plus(DatePeriod(days = 15))

        for (date in fifteenDaysAgo..fifteenDaysAhead) {
            val entry = DateRowEntry(
                localDate = date,
                dayOfTheMonth = date.day,
                dayOfWeek = date.dayOfWeek
            )
            entries.add(entry)
        }

        _state.update { it.copy(
            currentDate = today,
            dateRowEntries = entries
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



