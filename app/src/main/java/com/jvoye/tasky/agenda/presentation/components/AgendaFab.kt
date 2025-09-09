package com.jvoye.tasky.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.AgendaType
import com.jvoye.tasky.agenda.presentation.util.AgendaFabMenuEntry
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bell
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Calendar
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Circle_Check
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_X
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_plus
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun AgendaFab(
    modifier: Modifier = Modifier,
    onFabMenuItemClick: (AgendaType) -> Unit,
) {
    val fabShadowColor = Color(0xFF16161C).copy(alpha = 0.2f)

    val items = listOf(
        AgendaFabMenuEntry(
            agendaType = AgendaType.EVENT,
            leadingIcon = Icon_Calendar,
            label = stringResource(R.string.event)
        ),
        AgendaFabMenuEntry(
            agendaType = AgendaType.TASK,
            leadingIcon = Icon_Circle_Check,
            label = stringResource(R.string.task)
        ),
        AgendaFabMenuEntry(
            agendaType = AgendaType.REMINDER,
            leadingIcon = Icon_Bell,
            label = stringResource(R.string.reminder)
        )
    )
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.BottomEnd)
    ) {
        var isFabMenuExpanded by remember { mutableStateOf(false) }
        // FAB
        Box(
            modifier = Modifier
                .clickable { isFabMenuExpanded = !isFabMenuExpanded }
                .size(68.dp)
                .dropShadow(
                    shape = RoundedCornerShape(20.dp)
                ) {
                    radius = 6f
                    color = fabShadowColor
                    spread = 0f
                    offset = Offset(x = 2f, y = 2f)
                }
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = if (isFabMenuExpanded) Icon_X else Icon_plus,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        DropdownMenu(
            expanded = isFabMenuExpanded,
            onDismissRequest = { isFabMenuExpanded = false },
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = { onFabMenuItemClick(item.agendaType) },
                    leadingIcon = {
                        Icon(
                            imageVector = item.leadingIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    contentPadding = PaddingValues(start = 12.dp, end = 40.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun AgendaFabPreview() {
    TaskyTheme {
        AgendaFab(
            onFabMenuItemClick = {},
        )
    }
}


