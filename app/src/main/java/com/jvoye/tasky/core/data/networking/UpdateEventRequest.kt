package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class UpdateEventRequest(
    val title: String,
    val description: String?,
    val from: String,
    val to: String,
    val remindAt: String,
    val attendeeIds: List<String>,
    val newPhotoKeys: List<String>,
    val deletedPhotoKeys: List<String>,
    val isGoing: Boolean,
    val updatedAt: String
)