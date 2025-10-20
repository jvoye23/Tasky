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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDeleteButton
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyOutlinedButton
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.util.DeviceConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaItemDeleteDialog(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onToggleDeleteDialog: () -> Unit,
    isDeleteButtonLoading: Boolean,
) {
    val sheetState = rememberModalBottomSheetState()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when(deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            ModalBottomSheet(
                onDismissRequest = { onToggleDeleteDialog() },
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                SheetContent(
                    onToggleDeleteBottomSheet = onToggleDeleteDialog,
                    onDelete = onDelete,
                    isDeleteButtonLoading = isDeleteButtonLoading
                )
            }

        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {

        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Dialog(
                onDismissRequest = { onToggleDeleteDialog() },
                content = {
                    DialogContent(
                        modifier = modifier,
                        onToggleDeleteDialog = onToggleDeleteDialog,
                        onDelete = onDelete,
                        isDeleteButtonLoading = isDeleteButtonLoading
                    )
                },
                properties = DialogProperties()
            )
        }
    }


}

@Composable
private fun DialogContent(
    modifier: Modifier = Modifier,
    onToggleDeleteDialog: () -> Unit,
    onDelete: () -> Unit,
    isDeleteButtonLoading: Boolean,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),

            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.delete_question),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.this_action_cannot_be_reversed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TaskyOutlinedButton(
                onClick = { onToggleDeleteDialog() },
                modifier = Modifier
                    .weight(.5f),
                text = stringResource(R.string.cancel).uppercase(),
            )
            TaskyDeleteButton(
                onClick = { onDelete() },
                text = stringResource(R.string.delete).uppercase(),
                isLoading = isDeleteButtonLoading,
                modifier = Modifier
                    .weight(.5f)
            )
        }
    }
}

@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    onToggleDeleteBottomSheet: () -> Unit,
    onDelete: () -> Unit,
    isDeleteButtonLoading: Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.delete_question),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.this_action_cannot_be_reversed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TaskyOutlinedButton(
                onClick = { onToggleDeleteBottomSheet() },
                modifier = Modifier
                    .weight(.5f),
                text = stringResource(R.string.cancel).uppercase(),
            )
            TaskyDeleteButton(
              onClick = { onDelete() },
                text = stringResource(R.string.delete).uppercase(),
                isLoading = isDeleteButtonLoading,
                modifier = Modifier
                    .weight(.5f)
            )
        }
    }
}

@Preview(device = TABLET)
@Composable
private fun SheetContentPreview() {
    TaskyTheme {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AgendaItemDeleteDialog(
                onDelete = {},
                onToggleDeleteDialog = {},
                isDeleteButtonLoading = false

            )
        }
    }
}