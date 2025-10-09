package com.jvoye.tasky.agenda.domain


import com.jvoye.tasky.core.domain.model.User
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result

interface AttendeeManager {
    suspend fun fetchUser(email: String): Result<User, DataError.Network>
    suspend fun removeUser(eventId: String): EmptyResult<DataError>
}