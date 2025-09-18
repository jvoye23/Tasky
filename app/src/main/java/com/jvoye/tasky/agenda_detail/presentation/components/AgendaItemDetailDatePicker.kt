@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda_detail.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailAction

@Composable
fun AgendaDetailDatePicker(
    onAction: (AgendaDetailAction) -> Unit,
    datePickerState: DatePickerState
) {
    DatePickerDialog(
        onDismissRequest = {
            onAction(AgendaDetailAction.OnDismissDatePickerDialog)
        },
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    onAction(AgendaDetailAction.ConfirmDateSelection(selectedDateMillis = datePickerState.selectedDateMillis!!))
                }
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onAction(AgendaDetailAction.OnDismissDatePickerDialog)
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = stringResource(R.string.select_a_date),
                    modifier = Modifier
                        .padding(24.dp)
                )
            }
        )
    }
}