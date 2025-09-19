package com.jvoye.tasky.agenda_detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda_detail.presentation.components.EditTextTopAppBar
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun EditTextScreenRoot(
    editText: String,
    isTitle: Boolean,
    onCancelClick: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    EditTextScreen(
        editText = editText,
        isTitle = isTitle,
        onCancelClick = onCancelClick,
        onSaveClick = onSaveClick
    )
}

@Composable
private fun EditTextScreen(
    editText: String,
    isTitle: Boolean,
    onCancelClick: () -> Unit,
    onSaveClick: (updatedText: String) -> Unit
) {
    var currentText by remember { mutableStateOf(editText) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            EditTextTopAppBar(
                onCancelClick = onCancelClick,
                onSaveClick = { onSaveClick(currentText) },
                title = if(isTitle) stringResource(R.string.title) else stringResource(R.string.description),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            OutlinedTextField(
                value = currentText,
                onValueChange = { currentText = it },
                placeholder = {
                    Text(
                        text = if(isTitle) stringResource(R.string.title) else stringResource(R.string.description),
                        style = if(isTitle) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium,
                    )
                },
                textStyle = if(isTitle) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium,
                shape = RectangleShape,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                    disabledIndicatorColor = MaterialTheme.colorScheme.surface,
                    errorIndicatorColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = isTitle,
                modifier = if (isTitle) Modifier.fillMaxWidth() else Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
private fun EditTextScreenPreview() {
    TaskyTheme {
        EditTextScreen(
            editText = "My Title",
            isTitle = true,
            onCancelClick = {},
            onSaveClick = {}
        )
    }
}