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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.AgendaAction
import com.jvoye.tasky.agenda.presentation.AgendaState
import com.jvoye.tasky.agenda.presentation.mappers.toFabEntry
import com.jvoye.tasky.agenda.presentation.model.AgendaFabMenuItemUi
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_X
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_plus
import com.jvoye.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun AgendaFab(
    modifier: Modifier = Modifier,
    action: (AgendaAction) -> Unit,
    state: AgendaState,
    menuItems: List<AgendaFabMenuItemUi> = TaskyType.entries.map { it.toFabEntry() }
) {
    val fabShadowColor = Color(0xFF16161C).copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.BottomEnd)
    ) {
        // FAB toggle
        Box(
            modifier = Modifier
                .clickable { action(AgendaAction.OnToggleAgendaFabMenu) }
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
                imageVector = if (state.isFabMenuExpanded) Icon_X else Icon_plus,
                contentDescription = stringResource(R.string.add),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        DropdownMenu(
            expanded = state.isFabMenuExpanded,
            onDismissRequest = { action(AgendaAction.OnToggleAgendaFabMenu) },
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.label.asString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        action(AgendaAction.OnToggleAgendaFabMenu)
                        action(AgendaAction.OnFabMenuItemClick(true, item.taskyType))
                    },
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
            action = {},
            state = AgendaState()
        )
    }
}


