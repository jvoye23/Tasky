package com.jvoye.tasky.core.data.auth

import android.content.Context
import androidx.datastore.dataStore
import com.jvoye.tasky.core.domain.AuthInfo
import com.jvoye.tasky.core.domain.SessionStorage
import kotlinx.coroutines.flow.first

private val Context.dataStore by dataStore(
    fileName = "api-token-storage",
    serializer = AuthInfoSerializer
)

class EncryptedSessionDataStore(
    val context: Context
): SessionStorage {

    override suspend fun get(): AuthInfo {
       return context.dataStore.data.first().toAuthInfo()
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
