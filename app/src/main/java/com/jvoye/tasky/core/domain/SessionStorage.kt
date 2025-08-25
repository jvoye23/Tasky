package com.jvoye.tasky.core.domain

import com.jvoye.tasky.core.domain.model.AuthInfo

interface SessionStorage {
    suspend fun get(): AuthInfo?
    suspend fun set(info: AuthInfo?)
}

