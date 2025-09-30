@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda.presentation.agenda_list.components

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
import com.jvoye.tasky.agenda.presentation.agenda_list.AgendaAction

@Composable
fun AgendaDatePicker(
    action: (AgendaAction) -> Unit,
    datePickerState: DatePickerState
) {
    DatePickerDialog(
        onDismissRequest = {
            action(AgendaAction.OnDismissDatePickerDialog)
        },
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    action(AgendaAction.ConfirmDateSelection(selectedDateMillis = datePickerState.selectedDateMillis!!))
                }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    action(AgendaAction.OnDismissDatePickerDialog)
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