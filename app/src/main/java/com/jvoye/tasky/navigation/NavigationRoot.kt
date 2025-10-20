package com.jvoye.tasky.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

import androidx.navigation3.ui.NavDisplay

import com.jvoye.tasky.PendingNavigation
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_list.AgendaScreenRoot
import com.jvoye.tasky.agenda.domain.EditTextType
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailAction
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailScreenRoot
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailScreenViewModel
import com.jvoye.tasky.agenda.presentation.agenda_details.EditTextScreenRoot
import com.jvoye.tasky.agenda.presentation.event_photo.EditPhotoScreenRoot
import com.jvoye.tasky.auth.presentation.login.LoginScreenRoot
import com.jvoye.tasky.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.jvoye.tasky.core.presentation.designsystem.theme.success


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
    val taskyItemId: String? = null,
    val editedText: String? = null,
    val editTextType: EditTextType? = null
): NavKey

@Serializable
data class EditTextNavKey(
    val editText: String?,
    val editTextType: EditTextType
): NavKey

@Serializable
data class EditPhotoNavKey(
    val photoPath: String,
    val photoIndex: Int,
): NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
        .background(MaterialTheme.colorScheme.background),
    isLoggedIn: Boolean,
    pendingNavigation: PendingNavigation?,
    onNavigationHandled: () -> Unit
) {
    val backStack = rememberNavBackStack(
        if (isLoggedIn) AgendaNavKey else RegisterNavKey
    )

    //Nav3 alpha11 changes
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 24.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)
    val twoPaneStrategy = rememberTwoPaneSceneStrategy<NavKey>()


    val editTextCallback = remember { mutableStateOf<((String) -> Unit)?>(null) }
    val deletePhotoCallback = remember { mutableStateOf<((Int) -> Unit)?>(null) }

    LaunchedEffect(pendingNavigation) {
        pendingNavigation?.let {
            backStack.add(AgendaDetailNavKey(
                isEditMode = false,
                taskyType = it.itemType,
                taskyItemId = it.itemId
            ))
            onNavigationHandled()
        }
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = listDetailStrategy,
        //sceneStrategy = twoPaneStrategy,
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
                        key= key,
                        //metadata = TaskyTwoPaneScene.twoPane()
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                Text("Choose an item from the list")
                            }
                        )
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
                                val newAgendaNavKey = AgendaNavKey


                                //Check if the last backstack entry is already a AgendaKey
                                val lastKey = backStack.lastOrNull()
                                if (lastKey is AgendaNavKey) {
                                    backStack.remove(lastKey)
                                }
                                backStack.add(newAgendaNavKey)

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
                        key= key,
                        metadata = ListDetailSceneStrategy.detailPane()
                        //metadata = TaskyTwoPaneScene.twoPane()
                    ) {
                        val detailVm: AgendaDetailScreenViewModel = koinViewModel {
                            parametersOf(key.isEditMode, key.taskyType, key.taskyItemId, key.editedText, key.editTextType)
                        }

                        AgendaDetailScreenRoot(
                            onCloseAndCancelClick = { backStack.remove(key) },
                            viewModel = detailVm,
                            onEditTextClick = { text, editTextType ->
                                editTextCallback.value = { newText ->
                                    detailVm.onAction(
                                        action = AgendaDetailAction.OnEditTextChanged(
                                            editTextType = editTextType,
                                            value = newText
                                        )
                                    )
                                }
                                backStack.add(EditTextNavKey(
                                    editText = text,
                                    editTextType = editTextType
                                    ))
                            },
                            onEditPhotoClick = { photoPath, photoIndex ->
                                deletePhotoCallback.value = { photoIndex ->
                                    detailVm.onAction(
                                        action = AgendaDetailAction.OnDeletePhoto(
                                            photoIndex = photoIndex
                                        )
                                    )
                                }
                                backStack.add(EditPhotoNavKey(
                                    photoPath = photoPath,
                                    photoIndex = photoIndex
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
                is EditPhotoNavKey -> {
                    NavEntry(
                        key = key
                    ) {
                        EditPhotoScreenRoot(
                            onCloseAndCancelClick = { backStack.remove(key) },
                            viewModel = koinViewModel {
                                parametersOf(key.photoPath, key.photoIndex)
                            },
                            onDeletePhotoClick = { photoIndex ->
                                deletePhotoCallback.value?.invoke(photoIndex)
                                backStack.remove(key)
                            }
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}