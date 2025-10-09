package com.jvoye.tasky.agenda.presentation.event_photo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.agenda.presentation.event_photo.EditPhotoAction
import com.jvoye.tasky.agenda.presentation.event_photo.EditPhotoState
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bin
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Edit
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Offline
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_X
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPhotoTopAppBar(
    title: String = "",
    onAction: (EditPhotoAction) -> Unit,
    state: EditPhotoState,

    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor
                )
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .size(40.dp),
                onClick = {
                    onAction(EditPhotoAction.OnCloseAndCancelClick)
                },
                content = {
                    Icon(
                        imageVector = Icon_X,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .size(40.dp),
                onClick = {
                    onAction(editPhotoAction(state))
                },
                content = {
                    Icon(
                        imageVector = editPhotoActionIcon(state),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            )

        },
        colors = TopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}

@Composable
private fun editPhotoActionIcon(state: EditPhotoState): ImageVector = when {
    !state.isEditMode -> Icon_Edit
    state.isOnline -> Icon_Bin
    else -> Icon_Offline
}

private fun editPhotoAction(state: EditPhotoState): EditPhotoAction = when {
    !state.isEditMode -> EditPhotoAction.OnToggleEditMode
    state.isOnline -> EditPhotoAction.OnDeleteClick
    else -> EditPhotoAction.OnToggleEditMode
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
            EditPhotoTopAppBar(
                title = "EDIT TASK",
                onAction = {},
                state = EditPhotoState(
                    isEditMode = true,
                    isOnline = false,
                    titleText = "01 MARCH 2022"
                )
            )
        }
    }
}