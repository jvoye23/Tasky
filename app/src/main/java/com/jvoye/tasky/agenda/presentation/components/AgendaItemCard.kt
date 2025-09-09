package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.background
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
import com.jvoye.tasky.agenda.domain.AgendaItem
import com.jvoye.tasky.agenda.domain.AgendaMenuType
import com.jvoye.tasky.agenda.domain.AgendaType
import com.jvoye.tasky.agenda.presentation.AgendaAction
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Awaiting
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Done
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_More
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.jvoye.tasky.core.presentation.designsystem.theme.agendaItemFinished
import com.jvoye.tasky.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun AgendaItemCard(
    modifier: Modifier = Modifier,
    agendaItem: AgendaItem,
    onAgendaItemClick: (agendaItemId: Int) -> Unit,
    onAgendaItemMenuClick: (agendaItemId: Int) -> Unit,
    action: (AgendaAction) -> Unit,
) {
    val menuItems = listOf(
        AgendaMenuType.OPEN to stringResource(R.string.open),
        AgendaMenuType.EDIT to stringResource(R.string.edit),
        AgendaMenuType.DELETE to stringResource(R.string.delete)
    )           

    val agendaItemTextColor = when(agendaItem.agendaItemType) {
        AgendaType.TASK -> MaterialTheme.colorScheme.onBackground
        AgendaType.EVENT -> MaterialTheme.colorScheme.background
        AgendaType.REMINDER -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(124.dp)
            .background(
                color = when(agendaItem.agendaItemType) {
                    AgendaType.EVENT -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                    AgendaType.TASK -> MaterialTheme.colorScheme.secondary
                    AgendaType.REMINDER -> MaterialTheme.colorScheme.surfaceHigher.copy(alpha = 0.8f)
                },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                top = 8.dp,
                bottom = 16.dp
            )
            .padding(start = if (agendaItem.agendaItemType != AgendaType.TASK) 16.dp else 0.dp)
            .padding(end = 16.dp)
    ) {
        if (agendaItem.agendaItemType == AgendaType.TASK) {
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(
                    onClick = { action(AgendaAction.OnAgendaTaskFinishedClick(agendaItem.agendaItemId)) },
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = if (agendaItem.isAgendaItemFinished) Icon_Done else Icon_Awaiting,
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
                    text = agendaItem.agendaItemTitle,
                    style = if (agendaItem.isAgendaItemFinished) MaterialTheme.typography.agendaItemFinished else MaterialTheme.typography.headlineMedium,
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
                                        text = item.second,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onClick = {  },
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
                    text = agendaItem.agendaItemDescription,
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
                    text = agendaItem.agendaItemDate,
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
                agendaItem = AgendaItem(
                    agendaItemId = 1,
                    agendaItemType = AgendaType.TASK,
                    agendaItemTitle = "Event 1",
                    agendaItemDescription = "Description for Event 1",
                    agendaItemDate = "Mar 5, 10:00 - Mar 07, 11:59",
                    isAgendaItemFinished = true
                ),
                action = {},
                onAgendaItemClick = {},
                onAgendaItemMenuClick = {}
            )
        }
    }
}