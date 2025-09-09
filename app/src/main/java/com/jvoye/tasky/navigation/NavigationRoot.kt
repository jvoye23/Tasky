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
import com.jvoye.tasky.agenda.domain.AgendaType
import com.jvoye.tasky.agenda.presentation.AgendaScreenRoot
import com.jvoye.tasky.auth.presentation.login.LoginScreenRoot
import com.jvoye.tasky.auth.presentation.register.RegisterScreenRoot
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailScreenRoot
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data object RegisterScreen: NavKey

@Serializable
data object LoginScreen: NavKey

@Serializable
data object AgendaScreen: NavKey

@Serializable
data class AgendaDetailScreen(val type: AgendaType): NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean
) {
   val backStack = rememberNavBackStack(RegisterScreen)
   if (isLoggedIn) {
       backStack.clear()
       backStack.add(AgendaScreen)
   }

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
                            onSuccessfulRegistration = { backStack.add(LoginScreen) }
                        )
                    }
                }
                is LoginScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        LoginScreenRoot(
                            onSignUpClick = { backStack.remove(LoginScreen) },
                            onSuccessfulLogin = {
                                backStack.clear()
                                backStack.add(AgendaScreen)
                            },
                            viewModel = koinViewModel()
                        )
                    }
                }
                is AgendaScreen -> {
                    NavEntry(
                        key= key
                    ) {
                        AgendaScreenRoot(
                            onSuccessfulLogout = {
                                backStack.clear()
                                backStack.add(LoginScreen)
                            },
                            viewModel = koinViewModel(),
                            onFabMenuItemClick = { agendaType ->
                                backStack.add(AgendaDetailScreen(agendaType))
                            }
                        )
                    }
                }
                is AgendaDetailScreen -> {
                    NavEntry(
                        key= key
                    ) {
                        AgendaDetailScreenRoot(
                            viewModel = koinViewModel {
                                parametersOf(key.type)
                            }
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}