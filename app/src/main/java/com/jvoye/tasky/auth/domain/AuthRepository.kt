package com.jvoye.tasky.auth.domain

import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun register(fullName: String, email: String, password: String): EmptyResult<DataError.Network>
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun logout(): EmptyResult<DataError.Network>
}