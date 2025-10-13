package com.jvoye.tasky.agenda.presentation.event_photo

data class EditPhotoState(
    val isEditMode: Boolean = false,
    val isOnline: Boolean = true,
    val titleText: String = "",
    val photoPath: String = "",
    val photoIndex: Int = 0
)

