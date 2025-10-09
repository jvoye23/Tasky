package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AttendeeManager
import com.jvoye.tasky.core.data.networking.UserDto
import com.jvoye.tasky.core.data.networking.delete
import com.jvoye.tasky.core.data.networking.get
import com.jvoye.tasky.core.data.networking.mappers.toUser
import com.jvoye.tasky.core.domain.model.User
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.map
import io.ktor.client.HttpClient

class KtorAttendeeManager(
    private val httpClient: HttpClient
): AttendeeManager {
    override suspend fun fetchUser(email: String): Result<User, DataError.Network> {
        return httpClient.get<UserDto>(
            route = "/attendee",
            queryParameters = mapOf("email" to email)
        ).map { userDto ->
            userDto.toUser()
        }
    }

    override suspend fun removeUser(eventId: String): EmptyResult<DataError> {
        return httpClient.delete(
            route = "/attendee",
            queryParameters = mapOf("eventId" to eventId)
        )
    }
}