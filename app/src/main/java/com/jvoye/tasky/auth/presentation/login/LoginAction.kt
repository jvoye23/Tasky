package com.jvoye.tasky.auth.presentation.login

import com.jvoye.tasky.auth.presentation.register.RegisterAction

sealed  interface LoginAction {
    data object OnTogglePasswordVisibilityClick: LoginAction
    data object OnLoginClick: LoginAction
    data object OnSignUpClick: LoginAction
}