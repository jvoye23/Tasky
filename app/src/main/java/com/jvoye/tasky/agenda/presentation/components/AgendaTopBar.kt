@file:OptIn(ExperimentalMaterial3Api::class)

package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.agenda.presentation.AgendaAction
import com.jvoye.tasky.agenda.presentation.AgendaState
import com.jvoye.tasky.core.presentation.designsystem.buttons.TaskyDateTimePicker
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Calendar_Today

@Composable
fun AgendaTopBar(
    state: AgendaState,
    action: (AgendaAction) -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        title = {
            TaskyDateTimePicker(
                text = state.currentMonthName,
                onClick = {
                    action(AgendaAction.OnMonthTextClick)
                },
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                textStyle = MaterialTheme.typography.labelMedium,
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        actions = {

            IconButton(
                modifier = Modifier
                    .size(40.dp),
                onClick = {
                    action(AgendaAction.OnCalendarIconClick)
                }
            ) {
                Icon(
                    imageVector = Icon_Calendar_Today,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .clickable {
                            action(AgendaAction.OnUserInitialsClick)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.userInitials,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AgendaLogoutDropdown(
                    expanded = state.isLogoutDropdownVisible,
                    onLogOutClick = {
                        action(AgendaAction.OnLogOutClick)
                    },
                    onDismissRequest = {
                        action(AgendaAction.OnDismissAgendaLogoutDropdown)
                    }
                )
            }
        }
    )
}