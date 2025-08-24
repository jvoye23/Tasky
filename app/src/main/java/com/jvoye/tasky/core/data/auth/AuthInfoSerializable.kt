package com.jvoye.tasky.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userId: String? = null,
    val username: String? = null
)