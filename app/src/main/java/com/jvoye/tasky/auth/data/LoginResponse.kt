package com.jvoye.tasky.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val username: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)

