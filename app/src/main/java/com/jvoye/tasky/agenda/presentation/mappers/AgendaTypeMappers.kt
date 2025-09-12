package com.jvoye.tasky.agenda.presentation.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.model.AgendaFabMenuItemUi
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Bell
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Calendar
import com.jvoye.tasky.core.presentation.designsystem.theme.Icon_Circle_Check
import com.jvoye.tasky.core.presentation.designsystem.util.UiText


fun TaskyType.toUiText(): UiText = when (this) {
    TaskyType.EVENT -> UiText.StringResource(id = R.string.event)
    TaskyType.TASK -> UiText.StringResource(id = R.string.task)
    TaskyType.REMINDER -> UiText.StringResource(id = R.string.reminder)
}

@Composable
fun TaskyType.icon(): ImageVector = when (this) {
    TaskyType.EVENT -> Icon_Calendar
    TaskyType.TASK -> Icon_Circle_Check
    TaskyType.REMINDER -> Icon_Bell
}

@Composable
fun TaskyType.toFabEntry(): AgendaFabMenuItemUi =
    AgendaFabMenuItemUi(
        taskyType = this,
        leadingIcon = icon(),
        label = toUiText()
    )