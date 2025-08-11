package com.jvoye.tasky.auth.data

import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.core.data.networking.post
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthRepository(
    private val httpClient: HttpClient
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
}