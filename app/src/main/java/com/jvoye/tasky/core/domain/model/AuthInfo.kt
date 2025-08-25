package com.jvoye.tasky.core.domain.model

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val username: String
)