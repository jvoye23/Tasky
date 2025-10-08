package com.jvoye.tasky.agenda.presentation.event_photo

data class EditPhotoState(
    val isEditMode: Boolean = false,
    val isOnline: Boolean = true,
    val titleText: String = "",
    val localPhotoPath: String? = null,
    val photoUrl: String? = null
)

