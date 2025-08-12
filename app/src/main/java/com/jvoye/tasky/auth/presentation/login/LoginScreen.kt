package com.jvoye.tasky.auth.presentation.login

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
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyFilledButton
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyPasswordTextField
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyTextField
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.link
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.designsystem.util.DeviceConfiguration
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreenRoot(
    onSignUpClick: () -> Unit,
    onSuccessfulLogin: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is LoginAction.OnSignUpClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
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
                    LoginHeaderSection(
                        modifier = Modifier
                            .padding(top = 40.dp, bottom = 36.dp),
                        headerText = stringResource(R.string.welcome_back)
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
                        LoginFormSection(
                            state = state,
                            onTogglePasswordVisibility = {
                                onAction(LoginAction.OnTogglePasswordVisibilityClick)
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        LoginButtonSection(
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
                        LoginHeaderSection(
                            headerText = stringResource(R.string.welcome_back_landscape)
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
                            .fillMaxHeight()
                    ) {
                        item {
                            LoginFormSection(
                                state = state,
                                onTogglePasswordVisibility = {
                                    onAction(LoginAction.OnTogglePasswordVisibilityClick)
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))

                            LoginButtonSection(
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
                    LoginHeaderSection(
                        modifier = Modifier
                            .padding(top = 248.dp, bottom = 36.dp),
                        headerText = stringResource(R.string.welcome_back)
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
                        LoginFormSection(
                            state = state,
                            onTogglePasswordVisibility = {
                                onAction(LoginAction.OnTogglePasswordVisibilityClick)
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        LoginButtonSection(
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
                    LoginHeaderSection(
                        modifier = Modifier
                            .padding(bottom = 36.dp),
                        headerText = stringResource(R.string.welcome_back)
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
                        LoginFormSection(
                            state = state,
                            onTogglePasswordVisibility = {
                                onAction(LoginAction.OnTogglePasswordVisibilityClick)
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        LoginButtonSection(
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginHeaderSection(
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
private fun LoginFormSection (
    state: LoginState,
    onTogglePasswordVisibility: () -> Unit
) {
    Column(
        modifier = Modifier
    ) {
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
                onTogglePasswordVisibility()
            },
            hint = stringResource(R.string.password),
            borderColor = MaterialTheme.colorScheme.surfaceHigher,
            passwordErrorLabel = state.passwordErrorText?.asString(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LoginButtonSection(
    onAction: (LoginAction) -> Unit
) {
    TaskyFilledButton(
        text = stringResource(R.string.login),
        onClick = { onAction(LoginAction.OnLoginClick) }
    )

    Spacer(modifier = Modifier.height(20.dp))

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        ) {
            append(stringResource(R.string.dont_have_an_account) + " ")
        }
        withLink(
            LinkAnnotation.Clickable(
                tag = "SignUp_click",
                linkInteractionListener = {
                    onAction(LoginAction.OnSignUpClick)
                }
            )
        ) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.link
                )
            ) {
                append(stringResource(R.string.sign_up))
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
@Composable
fun LoginScreenPreview() {
    TaskyTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}

