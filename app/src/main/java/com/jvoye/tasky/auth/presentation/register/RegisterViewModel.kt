package com.jvoye.tasky.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.R
import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.auth.domain.UserDataValidator
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.presentation.designsystem.util.textAsFlow
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
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

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
): ViewModel() {

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()
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
                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            val result = authRepository.register(
                fullName = state.value.name.text.toString(),
                email = state.value.email.text.toString().trim(),
                password = state.value.password.text.toString()
            )

            when(result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        eventChannel.send(RegisterEvent.Error(
                            UiText.StringResource(R.string.error_email_exists)
                        ))
                    } else {
                        eventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }
                is Result.Success<*> -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }

        }
    }
}