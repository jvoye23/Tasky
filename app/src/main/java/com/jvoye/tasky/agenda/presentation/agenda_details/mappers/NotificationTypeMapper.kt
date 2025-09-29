package com.jvoye.tasky.agenda.presentation.agenda_details.mappers

import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.core.presentation.designsystem.util.UiText


fun NotificationType.toUiText(): UiText = when (this) {
    NotificationType.TEN_MINUTES_BEFORE -> UiText.StringResource(id = R.string.ten_minutes_before)
    NotificationType.THIRTY_MINUTES_BEFORE -> UiText.StringResource(id = R.string.thirty_minutes_before)
    NotificationType.ONE_HOUR_BEFORE -> UiText.StringResource(id = R.string.one_hour_before)
    NotificationType.SIX_HOURS_BEFORE -> UiText.StringResource(id = R.string.six_hours_before)
    NotificationType.ONE_DAY_BEFORE -> UiText.StringResource(id = R.string.one_day_before)
}