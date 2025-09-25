package com.jvoye.tasky.agenda_detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailAction
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailState
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDeleteButton
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyOutlinedButton
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaItemDeleteBottomSheet(
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onAction(AgendaDetailAction.OnToggleDeleteBottomSheet)  },
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        SheetContent(
            onAction = onAction,
            state = state
        )
    }
}

@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState
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
                text = stringResource(R.string.delete_task_question),
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
                onClick = { onAction(AgendaDetailAction.OnToggleDeleteBottomSheet) },
                modifier = Modifier
                    .weight(.5f),
                text = stringResource(R.string.cancel).uppercase(),
            )
            TaskyDeleteButton(
              onClick = { /*TODO() Delete Item*/ },
                text = stringResource(R.string.delete).uppercase(),
                isLoading = state.isDeleteButtonLoading,
                modifier = Modifier
                    .weight(.5f)
            )
        }
    }
}

@Preview
@Composable
private fun SheetContentPreview() {
    TaskyTheme {
        SheetContent(
            onAction = {},
            state = AgendaDetailState()
        )
    }
}