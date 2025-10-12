package com.jvoye.tasky.agenda.data

import com.jvoye.tasky.agenda.domain.AttendeeManager
import com.jvoye.tasky.core.data.networking.dto.LookupAttendeeDto
import com.jvoye.tasky.core.data.networking.delete
import com.jvoye.tasky.core.data.networking.get
import com.jvoye.tasky.core.data.networking.mappers.toLookupAttendee
import com.jvoye.tasky.core.domain.model.LookupAttendee
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result
import com.jvoye.tasky.core.domain.util.map
import io.ktor.client.HttpClient

class KtorAttendeeManager(
    private val httpClient: HttpClient
): AttendeeManager {
    override suspend fun fetchLookupAttendee(email: String): Result<LookupAttendee, DataError.Network> {
        return httpClient.get<LookupAttendeeDto>(
            route = "/attendee",
            queryParameters = mapOf("email" to email)
        ).map { lookupAttendeeDto ->
            lookupAttendeeDto.toLookupAttendee()
        }
    }

    override suspend fun removeAttendee(eventId: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = "/attendee",
            queryParameters = mapOf("eventId" to eventId)
        )
    }
}