package com.jvoye.tasky.agenda.presentation.agenda_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.AgendaMenuType
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_list.AgendaAction
import com.jvoye.tasky.agenda.presentation.agenda_list.mappers.getItemCardDateTimeString
import com.jvoye.tasky.agenda.presentation.agenda_list.mappers.toUiText
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Awaiting
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Done
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_More
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.agendaItemFinished
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher
import com.jvoye.tasky.core.presentation.designsystem.util.UiText
import kotlinx.datetime.LocalDateTime

@Composable
fun AgendaItemCard(
    modifier: Modifier = Modifier,
    taskyItem: TaskyItem,
    action: (AgendaAction) -> Unit,
    menuItems: List<UiText> = AgendaMenuType.entries.map { it.toUiText() }
) {
    val agendaItemTextColor = when(taskyItem.type) {
        TaskyType.TASK -> MaterialTheme.colorScheme.onBackground
        TaskyType.EVENT -> MaterialTheme.colorScheme.background
        TaskyType.REMINDER -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(124.dp)
            .clickable {
                action(AgendaAction.OnAgendaItemClick(false, taskyItem.type, taskyItem.id ))
            }
            .background(
                color = when(taskyItem.type) {
                    TaskyType.EVENT -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                    TaskyType.TASK -> MaterialTheme.colorScheme.secondary
                    TaskyType.REMINDER -> MaterialTheme.colorScheme.surfaceHigher.copy(alpha = 0.8f)
                },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                top = 8.dp,
                bottom = 16.dp
            )
            .padding(start = if (taskyItem.type != TaskyType.TASK) 16.dp else 0.dp)
            .padding(end = 16.dp)
    ) {
        if (taskyItem.type == TaskyType.TASK) {

            Column(
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(
                    onClick = { action(AgendaAction.OnAgendaTaskFinishedClick(taskyItem)) },
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = if (taskyItem.details is TaskyItemDetails.Task && taskyItem.details.isDone) Icon_Done else Icon_Awaiting,
                        contentDescription = stringResource(R.string.agenda_item_status_icon),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = taskyItem.title,
                    style = if (taskyItem.details is TaskyItemDetails.Task && taskyItem.details.isDone) MaterialTheme.typography.agendaItemFinished else MaterialTheme.typography.headlineMedium,
                    color = agendaItemTextColor
                )
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    var isMenuExpanded by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { isMenuExpanded = !isMenuExpanded },
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icon_More,
                            contentDescription = stringResource(R.string.agenda_item_more_icon),
                            tint = agendaItemTextColor,
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        shape = RoundedCornerShape(8.dp),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        menuItems.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = item.asString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onClick = {
                                    isMenuExpanded = false
                                    when(menuItems.indexOf(item)){
                                        0 -> action(AgendaAction.OnAgendaItemMenuClick(
                                            false, taskyItem.type, taskyItem.id
                                        ))
                                        1 -> action(AgendaAction.OnAgendaItemMenuClick(
                                            true, taskyItem.type, taskyItem.id
                                        ))
                                        2 -> {
                                            action(AgendaAction.OnDeleteMenuItemClick(taskyItem))
                                        }
                                    }
                                },
                                contentPadding = PaddingValues(start = 12.dp, end = 40.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = taskyItem.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = agendaItemTextColor
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = getItemCardDateTimeString(taskyItem.time),
                    style = MaterialTheme.typography.bodySmall,
                    color = agendaItemTextColor
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AgendaItemPreview() {
    TaskyTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AgendaItemCard(
                taskyItem = TaskyItem(
                    id = "8",
                    title = "Task 4 Title",
                    description = "Task 4 description",
                    time = LocalDateTime(2023, 3, 1, 10, 0, 0, 0),
                    type = TaskyType.TASK,
                    details = TaskyItemDetails.Task(
                        isDone = true
                    ),
                    remindAt = LocalDateTime(2023, 1, 1, 11, 30),
                    notificationType = NotificationType.THIRTY_MINUTES_BEFORE
                ),
                action = {}
            )
        }
    }
}