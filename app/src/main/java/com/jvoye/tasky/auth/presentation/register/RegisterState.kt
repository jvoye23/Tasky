package com.jvoye.tasky.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.jvoye.tasky.auth.domain.PasswordValidationState
import com.jvoye.tasky.core.presentation.designsystem.util.UiText

data class RegisterState(
    val name: TextFieldState = TextFieldState(),
    val nameErrorText: UiText.StringResource? = null,
    val email: TextFieldState = TextFieldState(),
    val emailErrorText: UiText.StringResource? = null,
    val isValidName: Boolean = false,
    val isValidEmail: Boolean = false,
    val isValidPassword: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val passwordErrorText: UiText.StringResource? = null,
    val isRegistering: Boolean = false
)