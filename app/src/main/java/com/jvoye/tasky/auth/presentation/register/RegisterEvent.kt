package com.jvoye.tasky.auth.presentation.register

import com.jvoye.tasky.core.presentation.designsystem.util.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}