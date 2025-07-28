package com.jvoye.tasky.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibilityClick: RegisterAction
    data object OnGetStartedClick: RegisterAction
    data object OnLoginClick: RegisterAction
}