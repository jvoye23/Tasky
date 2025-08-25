package com.jvoye.tasky.core.data.auth

import com.jvoye.tasky.core.domain.model.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        username = username
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo? {
    return if (accessToken == null || refreshToken == null || userId == null || username == null) {
        null
    } else {
        AuthInfo(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            username = username
        )
    }
}