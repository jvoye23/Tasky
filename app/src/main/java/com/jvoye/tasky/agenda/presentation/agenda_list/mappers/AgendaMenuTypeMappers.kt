package com.jvoye.tasky.agenda.presentation.agenda_list.mappers

import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.AgendaMenuType
import com.jvoye.tasky.core.presentation.designsystem.util.UiText

fun AgendaMenuType.toUiText(): UiText = when (this) {
    AgendaMenuType.OPEN -> UiText.StringResource(id = R.string.open)
    AgendaMenuType.EDIT -> UiText.StringResource(id = R.string.edit )
    AgendaMenuType.DELETE -> UiText.StringResource(id = R.string.delete)
}