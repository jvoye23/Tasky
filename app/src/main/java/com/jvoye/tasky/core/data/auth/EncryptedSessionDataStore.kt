package com.jvoye.tasky.core.data.auth

import android.content.Context
import androidx.datastore.dataStore
import com.jvoye.tasky.core.domain.model.AuthInfo
import com.jvoye.tasky.core.domain.SessionStorage
import com.jvoye.tasky.core.presentation.mappers.toUserUi
import kotlinx.coroutines.flow.first

private val Context.dataStore by dataStore(
    fileName = "api-token-storage",
    serializer = AuthInfoSerializer
)

class EncryptedSessionDataStore(
    val context: Context
): SessionStorage {

    override suspend fun get(): AuthInfo? {
        val accessToken = context.dataStore.data.first().accessToken
        val refreshToken = context.dataStore.data.first().refreshToken
        val userId = context.dataStore.data.first().userId
        val username = context.dataStore.data.first().username

        val data = if (accessToken != null) {
            AuthInfoSerializable(
                accessToken = accessToken,
                refreshToken = refreshToken,
                userId = userId,
                username = username
            ).toAuthInfo()
        } else null
        return data
    }

    override suspend fun set(info: AuthInfo?) {
        context.dataStore.updateData {
            AuthInfoSerializable(
                accessToken = info?.accessToken,
                refreshToken = info?.refreshToken,
                userId = info?.userId
            )
        }.toAuthInfo()
    }
}
