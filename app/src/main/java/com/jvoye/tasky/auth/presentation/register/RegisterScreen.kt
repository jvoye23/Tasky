package com.jvoye.tasky.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyFilledButton
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyPasswordTextField
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyTextField
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.link
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun RegisterScreenRoot(

) {
    val viewModel = viewModel<RegisterViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterScreen(
        state = state,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            RegistrationHeaderSection()

            val rootModifier = Modifier
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    modifier = rootModifier

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

@Composable
private fun RegistrationHeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.create_your_account),
            style = MaterialTheme.typography.headlineLarge
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
            endIcon = if (state.isNameValid) {
                Icon_Check
            } else null,
            borderColor = if (!state.isNameValid){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            hint = stringResource(R.string.name),
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskyTextField(
            state = state.email,
            endIcon = if (state.isEmailValid) {
                Icon_Check
            } else null,
            borderColor = if (!state.isEmailValid){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            hint = stringResource(R.string.email_address),
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskyPasswordTextField(
            state = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = {
                onAction(RegisterAction.OnTogglePasswordVisibilityClick)
            },
            hint = stringResource(R.string.password)
        )
    }
}

@Composable
fun RegistrationButtonSection(
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

@Preview
@Composable
private fun RegisterScreenPreview() {
    TaskyTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        ) 
    }
}