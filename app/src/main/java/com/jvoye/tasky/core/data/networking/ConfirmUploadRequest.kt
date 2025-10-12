package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmUploadRequest(
    val uploadedKeys: List<String>,
)
