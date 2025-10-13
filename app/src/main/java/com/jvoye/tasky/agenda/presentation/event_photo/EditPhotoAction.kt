package com.jvoye.tasky.agenda.presentation.event_photo

interface EditPhotoAction {
    data object OnCloseAndCancelClick: EditPhotoAction
    data class OnDeletePhotoClick(val photoIndex: Int): EditPhotoAction
    data object OnToggleEditMode: EditPhotoAction
}