package com.jvoye.tasky.agenda.presentation.agenda_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailAction
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailState
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.toUiText
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bell
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Dropdown
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.success
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun AgendaItemDetailNotificationDropdown(
    modifier: Modifier = Modifier,
    onAction: (AgendaDetailAction) -> Unit,
    state: AgendaDetailState,
    dropdownItems: List<NotificationType> = NotificationType.entries
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { if (state.isEditMode) onAction(AgendaDetailAction.OnToggleNotificationDropdown) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceHigher,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icon_Bell,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            modifier = Modifier
                .weight(1f),
            text = state.notificationType.toUiText().asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Icon(
                imageVector = Icon_Dropdown,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp),
                tint = if (state.isEditMode) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.primary.copy(alpha = 0f)
            )

            DropdownMenu(
                expanded = state.isNotificationDropdownExpanded,
                onDismissRequest = { onAction(AgendaDetailAction.OnToggleNotificationDropdown) },
                offset = DpOffset(0.dp, 8.dp),
                shape = RoundedCornerShape(8.dp),
                containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceHigher
                    else  MaterialTheme.colorScheme.surface,
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .background( color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceHigher
                                else if(state.notificationType == item) MaterialTheme.colorScheme.surfaceHigher
                                else MaterialTheme.colorScheme.surface
                            ),
                        text = {
                            Text(
                                modifier = Modifier
                                    .padding(end = 16.dp),
                                text = item.toUiText().asString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icon_Check,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp),
                                tint = if(state.notificationType == item) MaterialTheme.colorScheme.success
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                            )
                        },
                        onClick = {
                            onAction(
                                AgendaDetailAction.OnNotificationItemClick(
                                notificationType = item
                            ))
                        },
                        contentPadding = PaddingValues(start = 16.dp, end = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AgendaItemDetailNotificationDropdownPreview() {
    TaskyTheme {
        AgendaItemDetailNotificationDropdown(
            onAction = {},
            state = AgendaDetailState(
                isEditMode = true,
                isNotificationDropdownExpanded = true
            )
        )
    }
}