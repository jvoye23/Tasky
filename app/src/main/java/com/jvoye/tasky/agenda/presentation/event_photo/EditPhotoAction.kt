package com.jvoye.tasky.agenda.presentation.event_photo

interface EditPhotoAction {
    data object OnCloseAndCancelClick: EditPhotoAction
    data object OnDeleteClick: EditPhotoAction
}