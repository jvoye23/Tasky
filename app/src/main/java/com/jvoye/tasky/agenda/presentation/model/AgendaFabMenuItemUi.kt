package com.jvoye.tasky.agenda.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.presentation.designsystem.util.UiText

data class AgendaFabMenuItemUi(
    val taskyType: TaskyType,
    val leadingIcon: ImageVector,
    val label: UiText
)



