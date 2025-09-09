package com.jvoye.tasky.agenda.presentation.util

import androidx.compose.ui.graphics.vector.ImageVector
import com.jvoye.tasky.agenda.domain.AgendaType

data class AgendaFabMenuEntry(
    val agendaType: AgendaType,
    val leadingIcon: ImageVector,
    val label: String
)
