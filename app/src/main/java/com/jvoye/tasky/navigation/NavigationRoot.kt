package com.jvoye.tasky.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.AgendaScreenRoot
import com.jvoye.tasky.agenda_detail.domain.EditTextType
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailScreenRoot
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailScreenViewModel
import com.jvoye.tasky.agenda_detail.presentation.EditTextScreenRoot
import com.jvoye.tasky.auth.presentation.login.LoginScreenRoot
import com.jvoye.tasky.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data object RegisterNavKey: NavKey

@Serializable
data object LoginNavKey: NavKey

@Serializable
data object AgendaNavKey: NavKey

@Serializable
data class AgendaDetailNavKey(
    val isEditMode: Boolean,
    val taskyType: TaskyType,
    val taskyItemId: Long? = null,
    val editedText: String? = null,
    val editTextType: EditTextType? = null
): NavKey

@Serializable
data class EditTextNavKey(
    val editText: String, 
    val editTextType: EditTextType
): NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean
) {
    val backStack = rememberNavBackStack(
        if (isLoggedIn) AgendaNavKey else RegisterNavKey
    )
    val editTextCallback = remember { mutableStateOf<((String) -> Unit)?>(null) }

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
                is RegisterNavKey -> {
                    NavEntry(
                        key = RegisterNavKey
                    ) {
                        RegisterScreenRoot(
                            viewModel = koinViewModel(),
                            onLogInClick = { backStack.add(LoginNavKey) },
                            onSuccessfulRegistration = { backStack.add(LoginNavKey) }
                        )
                    }
                }
                is LoginNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        LoginScreenRoot(
                            onSignUpClick = { backStack.remove(LoginNavKey) },
                            onSuccessfulLogin = {
                                backStack.clear()
                                backStack.add(AgendaNavKey)
                            },
                            viewModel = koinViewModel()
                        )
                    }
                }
                is AgendaNavKey -> {
                    NavEntry(
                        key= key
                    ) {
                        AgendaScreenRoot(
                            onSuccessfulLogout = {
                                backStack.clear()
                                backStack.add(LoginNavKey)
                            },
                            viewModel = koinViewModel(),
                            onFabMenuItemClick = { isEdit, agendaType ->
                                backStack.add(AgendaDetailNavKey(
                                    isEditMode = true,
                                    taskyType = agendaType
                                    )
                                )
                            },
                            onAgendaItemClick = { _, agendaType, taskyItemId->
                                backStack.add(AgendaDetailNavKey(
                                    isEditMode = false,
                                    taskyType = agendaType,
                                    taskyItemId = taskyItemId
                                    )
                                )
                            },

                            onAgendaItemMenuClick = { isEdit, taskyType, taskyItemId ->
                                backStack.add(AgendaDetailNavKey(
                                    isEditMode = isEdit,
                                    taskyType = taskyType,
                                    taskyItemId = taskyItemId
                                    )
                                )
                            }
                        )
                    }
                }
                is AgendaDetailNavKey -> {
                    NavEntry(
                        key= key
                    ) {
                        val detailVm: AgendaDetailScreenViewModel = koinViewModel {
                            parametersOf(key.isEditMode, key.taskyType, key.taskyItemId, key.editedText, key.editTextType)
                        }

                        AgendaDetailScreenRoot(
                            onCloseAndCancelClick = { backStack.remove(key) },
                            viewModel = detailVm,
                            onEditTextClick = { text, editTextType ->
                                editTextCallback.value = { newText ->
                                    detailVm.updateEditedText(
                                        editedText = newText,
                                        editTextType = editTextType
                                    )
                                }
                                backStack.add(EditTextNavKey(
                                    editText = text,
                                    editTextType = editTextType
                                    ))
                            }
                        )
                    }
                }
                is EditTextNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        EditTextScreenRoot(
                            editText = key.editText,
                            onCancelClick = { backStack.remove(key) },
                            onSaveClick = { newText,_ ->
                                editTextCallback.value?.invoke(newText.trim())
                                backStack.remove(key)
                            },
                            editTextType = key.editTextType,
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}