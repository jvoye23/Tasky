package com.jvoye.tasky.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyFilledButton
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyPasswordTextField
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyTextField
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.link
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.designsystem.util.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = koinViewModel()

) {
    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->

        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

        when(deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    RegistrationHeaderSection(
                        modifier = Modifier
                            .padding(top = 40.dp, bottom = 36.dp),
                        headerText = stringResource(R.string.create_your_account)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 24.dp,
                                    topEnd = 24.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(
                                horizontal = 16.dp,
                                vertical = 28.dp
                            )
                            .consumeWindowInsets(WindowInsets.navigationBars)
                    ) {
                        RegistrationFormSection(
                            state = state,
                            onAction = onAction
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        RegistrationButtonSection(
                            onAction = onAction
                        )
                    }
                }
            }
            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                val bottomOffset: Dp = innerPadding.calculateTopPadding()
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.3f)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = bottomOffset),
                    ) {
                        RegistrationHeaderSection(
                            headerText = stringResource(R.string.create_your_account_landscape)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 24.dp,
                                    topEnd = 24.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(
                                vertical = 24.dp,
                                horizontal = 48.dp
                            )
                            .consumeWindowInsets(WindowInsets.navigationBars)
                    ) {
                        item {
                            RegistrationFormSection(
                                state = state,
                                onAction = onAction
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))

                            RegistrationButtonSection(
                                onAction = onAction
                            )
                        }
                    }
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RegistrationHeaderSection(
                        modifier = Modifier
                            .padding(top = 248.dp, bottom = 36.dp),
                        headerText = stringResource(R.string.create_your_account)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(
                                horizontal = 24.dp,
                                vertical = 32.dp
                            )
                            .consumeWindowInsets(WindowInsets.navigationBars)
                    ) {
                        RegistrationFormSection(
                            state = state,
                            onAction = onAction
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        RegistrationButtonSection(
                            onAction = onAction
                        )
                    }
                }
            }
            DeviceConfiguration.TABLET_LANDSCAPE,
            DeviceConfiguration.DESKTOP -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    RegistrationHeaderSection(
                        modifier = Modifier
                            .padding(bottom = 36.dp),
                        headerText = stringResource(R.string.create_your_account)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(
                                horizontal = 24.dp,
                                vertical = 32.dp
                            )
                            .consumeWindowInsets(WindowInsets.navigationBars)
                    ) {
                        RegistrationFormSection(
                            state = state,
                            onAction = onAction
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        RegistrationButtonSection(
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistrationHeaderSection(
    modifier: Modifier = Modifier,
    headerText: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = headerText,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegistrationFormSection (
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        TaskyTextField(
            state = state.name,
            endIcon = if (state.isValidName) {
                Icon_Check
            } else null,
            borderColor = if (!state.isValidName && state.name.text.isNotEmpty()){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            hint = stringResource(R.string.name),
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
            errorLabel = state.nameErrorText?.asString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskyTextField(
            state = state.email,
            endIcon = if (state.isValidEmail) {
                Icon_Check
            } else null,
            borderColor = if (!state.isValidEmail && state.email.text.isNotEmpty()){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            hint = stringResource(R.string.email_address),
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email,
            errorLabel = state.emailErrorText?.asString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskyPasswordTextField(
            state = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = {
                onAction(RegisterAction.OnTogglePasswordVisibilityClick)
            },
            hint = stringResource(R.string.password),
            borderColor = if (!state.passwordValidationState.isValidPassword && state.password.text.isNotEmpty()){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            passwordErrorLabel = state.passwordErrorText?.asString(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RegistrationButtonSection(
    onAction: (RegisterAction) -> Unit
) {
    TaskyFilledButton(
        text = stringResource(R.string.get_started),
        onClick = { onAction(RegisterAction.OnGetStartedClick) }
    )

    Spacer(modifier = Modifier.height(20.dp))

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        ) {
            append(stringResource(R.string.already_have_an_account) + " ")
        }
        withLink(
            LinkAnnotation.Clickable(
                tag = "Login_click",
                linkInteractionListener = {
                    onAction(RegisterAction.OnLoginClick)
                }
            )
        ) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.link
                )
            ) {
                append(stringResource(R.string.login))
            }
        }
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Preview(device = Devices.PIXEL_6_PRO, showSystemUi = true)
@Preview(device = Devices.PIXEL_TABLET, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    TaskyTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        ) 
    }
}