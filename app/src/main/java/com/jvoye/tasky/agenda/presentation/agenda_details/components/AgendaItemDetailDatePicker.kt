@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda.presentation.agenda_details.components

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

@Composable
fun AgendaDetailDatePicker(
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState,
    datePickerTitle: String = stringResource(R.string.select_a_date)
) {
    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    onConfirm(datePickerState.selectedDateMillis!!)
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
                onClick = { onDismiss() }
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
                    text = datePickerTitle,
                    modifier = Modifier
                        .padding(24.dp)
                )
            }
        )
    }
}