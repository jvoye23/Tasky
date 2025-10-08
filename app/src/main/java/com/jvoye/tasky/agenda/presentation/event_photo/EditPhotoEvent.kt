package com.jvoye.tasky.agenda.presentation.event_photo

import com.jvoye.tasky.core.presentation.designsystem.util.UiText

sealed interface EditPhotoEvent {
    data object PhotoDeleted: EditPhotoEvent
    data class Error(val error: UiText): EditPhotoEvent
}
