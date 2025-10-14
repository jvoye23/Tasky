package com.jvoye.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.SessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update



data class PendingNavigation(
    val itemId: String,
    val itemType: TaskyType
)

class
MainViewModel(
    private val sessionStorage: SessionStorage,
    private val agendaRepository: AgendaRepository
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {

                _state.update { it.copy(
                    isLoggedIn = sessionStorage.get() != null
                ) }

                //agendaRepository.fetchFullAgenda()

                _state.update { it.copy(
                    isCheckingAuth = false
                ) }

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    fun triggerNavigation(itemId: String, itemType: TaskyType) {
        _state.update {
            it.copy(
                pendingNavigation = PendingNavigation(
                    itemId = itemId,
                    itemType = itemType
                )
            )
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(pendingNavigation = null) }
    }
}
