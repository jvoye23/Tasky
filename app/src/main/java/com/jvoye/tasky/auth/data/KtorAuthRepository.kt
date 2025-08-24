package com.jvoye.tasky.auth.data

import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.core.data.networking.post
import com.jvoye.tasky.core.domain.model.AuthInfo
import com.jvoye.tasky.core.domain.SessionStorage
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class KtorAuthRepository(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository{
    override suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/auth/register",
            body = RegisterRequest(
                fullName = fullName,
                email = email,
                password = password
            )
        )
    }

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if(result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId,
                    username = result.data.username
                )
            )

        }
        return result.asEmptyDataResult()
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        val refreshToken = sessionStorage.get()?.refreshToken
        val result = httpClient.post<LogoutRequest, Unit>(
            route = "/auth/logout",
            body = LogoutRequest(
                refreshToken = refreshToken.toString()
            )
        )
        if (result is Result.Success) {
            sessionStorage.set(
                info = null
            )
        }
        return result.asEmptyDataResult()
    }
}