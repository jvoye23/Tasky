package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String,
    val fullName: String,
    val userId: String,
)
