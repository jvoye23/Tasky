package com.jvoye.tasky.auth.presentation.login

import com.jvoye.tasky.core.presentation.designsystem.util.UiText

sealed interface LoginEvent {
    data object LoginSuccess: LoginEvent
    data class Error(val error: UiText): LoginEvent
}