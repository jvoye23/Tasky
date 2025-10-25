@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.presentation.agenda_list.util.DateRowEntry
import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.core.data.auth.EncryptedSessionDataStore
import com.jvoye.tasky.core.domain.ConnectivityObserver
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.domain.notification.NotificationService
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import com.jvoye.tasky.core.presentation.mappers.toUserUi
import com.jvoye.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val agendaRepository: AgendaRepository,
    private val notificationService: NotificationService,
    private val connectivityObserver: ConnectivityObserver
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
                getTodaysAgendaItems()
                agendaRepository.fetchFullAgenda()
                toggleScreenLoading()
                observerConnectivity()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )



   /* private fun getOnlineStatus() {
        val isConnected = connectivityObserver.isConnected
        _state.update { it.copy(
            isOnline = isConnected
        ) }
    }*/

    private fun observerConnectivity() {
        connectivityObserver.isConnected
            .onEach { isConnected ->
                _state.update { it.copy(
                    isOnline = isConnected
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun toggleScreenLoading() {
        _state.update {
            it.copy(
                isScreenLoading = false
            )
        }
    }

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
                val selectedDate = action.selectedDate
                viewModelScope.launch {
                    agendaRepository.getTaskyItemsForDate(localDateString = selectedDate.toString())
                        .collect { taskyItems ->
                            _state.update { it.copy(
                                agendaList = taskyItems
                            )
                        }
                    }
                }
                _state.update { it.copy(
                    currentDate = selectedDate,
                    dateHeadline = selectedDate.toString()
                ) }
                getAgendaListTitle()
            }

            AgendaAction.OnToggleAgendaItemMoreMenu -> {
                _state.update { it.copy(
                    isAgendaItemMenuExpanded = it.isAgendaItemMenuExpanded.not()
                ) }
            }
            is AgendaAction.OnAgendaTaskFinishedClick -> {
                val taskyItem = action.taskyItem
                val isDone = (taskyItem.details as TaskyItemDetails.Task).isDone

                val updatedTaskyItem = taskyItem.copy(
                    details = (taskyItem.details as TaskyItemDetails.Task).copy(
                        isDone = !isDone
                    )
                )
                viewModelScope.launch {
                    agendaRepository.updateTaskyItem(updatedTaskyItem)
                }
            }
            is AgendaAction.OnDeleteClick -> {

                val taskyItem = state.value.taskyItemToBeDeleted

                viewModelScope.launch {
                    if (taskyItem != null) {
                        when(val result = agendaRepository.deleteTaskyItem(
                            taskyItemId = taskyItem.id,
                            taskyType = taskyItem.type
                        )) {
                            is Result.Error -> {
                                eventChannel.send(AgendaEvent.Error(result.error.asUiText()))
                            }

                            is Result.Success -> {
                                eventChannel.send(AgendaEvent.TaskyItemDeleted)
                            }
                        }
                    } else {
                        eventChannel.send(AgendaEvent.Error(UiText.StringResource(R.string.item_not_found)))
                    }
                    _state.update { it.copy(
                        isDeleteDialogVisible = false
                    )}
                }
            }

            AgendaAction.OnToggleAgendaFabMenu -> {
                _state.update {
                    it.copy(
                        isFabMenuExpanded = it.isFabMenuExpanded.not()
                    )
                }
            }
            is AgendaAction.OnToggleDeleteBottomSheet -> {
                _state.update { it.copy(
                    isDeleteDialogVisible = !it.isDeleteDialogVisible,
                    taskyItemToBeDeleted = null
                ) }
            }
            is AgendaAction.OnDeleteMenuItemClick -> {
                _state.update { it.copy(
                    isDeleteDialogVisible = true,
                    taskyItemToBeDeleted = action.taskyItem
                ) }
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

    private fun getTodaysAgendaItems() {
        // Example: today = 2025-10-14
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

        viewModelScope.launch {
            agendaRepository.getTaskyItemsForDate(localDateString = today)
                .collect { taskyItems ->
                    _state.update { it.copy(
                        agendaList = taskyItems
                    )
                }
            }
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

            agendaRepository.deleteAllLocalTaskyItems()

            val result = authRepository.logout()

            _state.update { it.copy(
                isLoggingOut = false,
            ) }

            when(result) {
                is Result.Error -> {
                    eventChannel.send(AgendaEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    //TODO Cancel all notification Ids
                    notificationService.cancelAllNotifications(notificationIds = emptyList())
                    eventChannel.send(AgendaEvent.LogoutSuccess)
                }
            }
        }
    }
}



