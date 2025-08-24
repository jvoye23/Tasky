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

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken.toString(),
        refreshToken = refreshToken.toString(),
        userId = userId.toString(),
        username = username.toString()
    )
}