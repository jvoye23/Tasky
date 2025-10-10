package com.jvoye.tasky.agenda.presentation.agenda_details.components

import com.jvoye.tasky.R
import com.jvoye.tasky.core.presentation.designsystem.util.UiText

enum class AttendeeFilterType(val label: UiText) {
    ALL( label = UiText.StringResource(R.string.all)),
    GOING( label = UiText.StringResource(R.string.going)),
    NOT_GOING( label = UiText.StringResource(R.string.not_going))
}