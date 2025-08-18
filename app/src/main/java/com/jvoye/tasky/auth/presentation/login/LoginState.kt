package com.jvoye.tasky.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import com.jvoye.tasky.core.presentation.designsystem.util.UiText

data class LoginState (
    val email: TextFieldState = TextFieldState(),
    val emailErrorText: UiText.StringResource? = null,
    val isValidEmail: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isValidPassword: Boolean = false,
    val passwordErrorText: UiText.StringResource? = null,
    val isLoggingIn: Boolean = false
)