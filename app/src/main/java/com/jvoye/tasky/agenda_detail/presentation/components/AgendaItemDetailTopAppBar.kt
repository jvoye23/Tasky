package com.jvoye.tasky.agenda_detail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailAction
import com.jvoye.tasky.agenda_detail.presentation.mappers.getItemDetailDateString
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Edit
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_X
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.success
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaItemDetailTopAppBar(
    modifier: Modifier = Modifier,
    isEditMode: Boolean,
    taskyItem: TaskyItem?,
    onAction: (AgendaDetailAction) -> Unit

) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    // TODO() Replace DateTime string to date set in Agenda Screen
                    text = if (isEditMode){
                        stringResource(R.string.edit_uppercase) + " " + taskyItem?.type.toString().uppercase()
                    } else getItemDetailDateString(taskyItem?.time).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        navigationIcon = {
            if (isEditMode) {
                TextButton(
                    onClick = { onAction(AgendaDetailAction.OnCloseAndCancelClick) },
                    modifier = Modifier,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {
                        onAction(AgendaDetailAction.OnCloseAndCancelClick)
                    },
                    content = {
                        Icon(
                            imageVector = Icon_X,
                            contentDescription = null
                        )
                    }
                )
            }

        },
        actions = {
            if (isEditMode) {
                TextButton(
                    onClick = { onAction(AgendaDetailAction.OnSaveClick) } ,
                    modifier = Modifier,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.success
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {
                        onAction(AgendaDetailAction.OnEditModeClick)
                    },

                    content = {
                        Icon(
                            imageVector = Icon_Edit,
                            contentDescription = null
                        )
                    }
                )
            }

        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,

            )
    )
}


@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun TopAppBarPreview() {
    TaskyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            AgendaItemDetailTopAppBar(
                isEditMode = true,
                taskyItem = TaskyItem(
                    id = 1,
                    title = "Task 1 Title",
                    description = "Task 1 description",
                    time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    type = TaskyType.TASK,
                    details = TaskyItemDetails.Task(
                        isDone = false
                    )
                ),

                onAction = {}
            )
        }

    }
    
}