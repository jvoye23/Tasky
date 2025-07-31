package com.jvoye.tasky.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.R
import com.jvoye.tasky.auth.domain.UserDataValidator
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterViewModel(
    private val userDataValidator: UserDataValidator
): ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    init {
        // Name validation
        state.name.textAsFlow()
            .onEach { name ->
                val isValidName = userDataValidator.isValidName(name.toString())
                state = state.copy(
                    isValidName = isValidName,
                    nameErrorText = if (!isValidName) {
                        UiText.StringResource(R.string.name_error_label)
                    } else null
                )
            }
            .launchIn(viewModelScope)

        // Email validation
        state.email.textAsFlow()
            .onEach { email ->
                val isValidEmail = userDataValidator.isValidEmail(email.toString())
                state = state.copy(
                    isValidPassword = isValidEmail,
                    emailErrorText = if (!isValidEmail) {
                        UiText.StringResource(R.string.email_error_label)
                    } else null
                )
            }
            .launchIn(viewModelScope)

        // Password validation
        state.password.textAsFlow()
            .onEach { password ->
                val passwordValidationState = userDataValidator.validatePassword(password.toString())
                state = state.copy(
                    passwordValidationState = passwordValidationState,
                    passwordErrorText = if (!passwordValidationState.isValidPassword) {
                        UiText.StringResource(R.string.password_error_label)
                    } else null
                )
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnLoginClick -> {

            }
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            RegisterAction.OnGetStartedClick -> {

            }
        }
    }
}

private fun TextFieldState.textAsFlow() = snapshotFlow { text }