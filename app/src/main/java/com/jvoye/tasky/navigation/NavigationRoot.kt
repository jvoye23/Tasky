package com.jvoye.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.jvoye.tasky.auth.presentation.login.LoginScreenRoot
import com.jvoye.tasky.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object RegisterScreen: NavKey

@Serializable
data object LoginScreen: NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(RegisterScreen)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is RegisterScreen -> {
                    NavEntry(
                        key = RegisterScreen
                    ) {
                        RegisterScreenRoot(
                            viewModel = koinViewModel(),
                            onLogInClick = { backStack.add(LoginScreen) },
                            onSuccessfulRegistration = { }
                        )
                    }
                }
                is LoginScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        LoginScreenRoot()
                    }
                }
                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}