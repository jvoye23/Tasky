package com.jvoye.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.core.domain.SessionStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val sessionStorage: SessionStorage
): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {

                _state.update { it.copy(
                    isLoggedIn = sessionStorage.get() != null
                ) }

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
}