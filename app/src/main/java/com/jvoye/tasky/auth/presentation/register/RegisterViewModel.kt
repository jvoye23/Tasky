package com.jvoye.tasky.auth.presentation.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel: ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnLoginClick -> {

            }
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                _state.update { it.copy(
                    isPasswordVisible = !it.isPasswordVisible
                )}
            }
            RegisterAction.OnGetStartedClick -> {

            }
        }
    }
}