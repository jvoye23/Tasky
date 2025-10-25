@file:OptIn(ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailAction
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailState
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyFilledButton
import com.jvoye.tasky.core.presentation.designsystem.textfields.TaskyTextField
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_X
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.designsystem.util.DeviceConfiguration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAttendeeDialog(
    modifier: Modifier = Modifier,
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            ModalBottomSheet(
                onDismissRequest = { onAction(AgendaDetailAction.OnToggleAddAttendeeBottomSheet) },
                modifier = modifier.fillMaxWidth(),
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                SheetContent(
                    state = state,
                    onAction = onAction,
                )
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            /*TODO() Setup for landscape*/
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Dialog(
                onDismissRequest = { onAction(AgendaDetailAction.OnToggleAddAttendeeBottomSheet) },
                content = {
                    DialogContent(
                        state = state,
                        onAction = onAction,
                    )
                }
            )
        }
    }


}

@Composable
private fun DialogContent(
    modifier: Modifier = Modifier,
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.add_visitor).uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterEnd),
                onClick = { onAction(AgendaDetailAction.OnToggleAddAttendeeBottomSheet) },
                content = {
                    Icon(
                        imageVector = Icon_X,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )
        Spacer(modifier = Modifier.height(28.dp))
        TaskyTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = state.emailInput,
            endIcon = if (state.doesEmailExist) {
                Icon_Check
            } else null,
            borderColor = if (!state.doesEmailExist && state.emailInput.text.isNotEmpty()){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            hint = stringResource(R.string.email_address),
            keyboardType = KeyboardType.Email,
            errorLabel = state.emailErrorText?.asString()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TaskyFilledButton(
                onClick = { onAction(AgendaDetailAction.OnAddAttendee(state.emailInput.text as String)) },
                text = stringResource(R.string.add).uppercase(),
                isLoading = state.isDeleteButtonLoading,
                modifier = Modifier
                    .weight(.5f),
                enabled = state.doesEmailExist
            )
        }

    }

}

@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.add_visitor).uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterEnd),
                onClick = { onAction(AgendaDetailAction.OnToggleAddAttendeeBottomSheet) },
                content = {
                    Icon(
                        imageVector = Icon_X,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceHigher)
        )
        Spacer(modifier = Modifier.height(28.dp))
        TaskyTextField(
            modifier = Modifier
                .fillMaxWidth(),
            state = state.emailInput,
            endIcon = if (state.doesEmailExist) {
                Icon_Check
            } else null,
            borderColor = if (!state.doesEmailExist && state.emailInput.text.isNotEmpty()){
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.surfaceHigher
            },
            hint = stringResource(R.string.email_address),
            keyboardType = KeyboardType.Email,
            errorLabel = state.emailErrorText?.asString()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TaskyFilledButton(
                onClick = { onAction(AgendaDetailAction.OnAddAttendee(state.emailInput.text as String)) },
                text = stringResource(R.string.add).uppercase(),
                isLoading = state.isDeleteButtonLoading,
                modifier = Modifier
                    .weight(.5f),
                enabled = state.doesEmailExist
            )
        }
    }
}

@Preview(Devices.TABLET)
@Composable
private fun SheetContentPreview() {
    TaskyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DialogContent(
                state = AgendaDetailState(),
                onAction = {}

            )
        }

        /*Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            SheetContent(
                modifier = Modifier,
                onAction = {},
                state = AgendaDetailState()
            )
        }*/
    }
}