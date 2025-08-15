package com.jvoye.tasky.core.domain

data class AuthInfo(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userId: String? = null
)
