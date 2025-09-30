@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.jvoye.tasky.agenda.presentation.agenda_details.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AgendaItemDetailTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true,
    )
    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                clockDialColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.outline,
                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.select_time),
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}




@Preview
@Composable
private fun AgendaItemDetailTimePickerPreview() {
    TaskyTheme {
        AgendaItemDetailTimePicker /**/(
            onConfirm = {},
            onDismiss = {}
        )

    }
}