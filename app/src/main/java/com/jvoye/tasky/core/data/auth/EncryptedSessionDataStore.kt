package com.jvoye.tasky.core.data.auth

import android.content.Context
import androidx.datastore.dataStore
import com.jvoye.tasky.core.domain.AuthInfo
import com.jvoye.tasky.core.domain.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.random.Random

private val Context.dataStore by dataStore(
    fileName = "api-token-storage",
    serializer = AuthInfoSerializer
)

class EncryptedSessionDataStore(
    val context: Context
): SessionStorage {

    override suspend fun get(): AuthInfo {
        return withContext(Dispatchers.IO) {
            context.dataStore.data.first().toAuthInfo()
        }
    }

    override suspend fun set(info: AuthInfo?) {
        withContext(Dispatchers.IO) {
            context.dataStore.updateData {
                AuthInfoSerializable(
                    accessToken = "access token " + Random.nextInt(100).toString(),
                    refreshToken = "refresh token " + Random.nextInt(100).toString(),
                    userId = "userId 1234"
                )
            }.toAuthInfo()
        }
    }
}
