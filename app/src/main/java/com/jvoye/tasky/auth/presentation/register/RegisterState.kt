package com.jvoye.tasky.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.jvoye.tasky.auth.domain.PasswordValidationState

data class RegisterState(
    val name: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val isNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState()
)