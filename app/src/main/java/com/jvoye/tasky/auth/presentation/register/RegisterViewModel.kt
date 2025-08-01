package com.jvoye.tasky.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.R
import com.jvoye.tasky.auth.domain.UserDataValidator
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val userDataValidator: UserDataValidator
): ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                validateUserInput()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            _state.value
        )

    fun validateUserInput() {
        // Name validation
        _state.value.name.textAsFlow()
            .onEach { name ->
                val isValidName = userDataValidator.isValidName(name.toString())
                _state.update {
                    it.copy(
                        isValidName = isValidName,
                        nameErrorText = if (!isValidName) {
                            UiText.StringResource(R.string.name_error_label)
                        } else null
                    )
                }
            }
            .launchIn(viewModelScope)

        // Email validation
        _state.value.email.textAsFlow()
            .onEach { email ->
                val isValidEmail = userDataValidator.isValidEmail(email.toString())
                _state.update {
                    it.copy(
                        isValidEmail = isValidEmail,
                        emailErrorText = if (!isValidEmail) {
                            UiText.StringResource(R.string.email_error_label)
                        } else null
                    )
                }
            }
            .launchIn(viewModelScope)

        // Password validation
        _state.value.password.textAsFlow()
            .onEach { password ->
                val passwordValidationState =
                    userDataValidator.validatePassword(password.toString())
                _state.update {
                    it.copy(
                        passwordValidationState = passwordValidationState,
                        passwordErrorText = if (!passwordValidationState.isValidPassword) {
                            UiText.StringResource(R.string.password_error_label)
                        } else null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnLoginClick -> {

            }
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
            RegisterAction.OnGetStartedClick -> {

            }
        }
    }
}

private fun TextFieldState.textAsFlow() = snapshotFlow { text }