package com.jvoye.tasky.agenda.domain


import com.jvoye.tasky.core.domain.model.LookupAttendee
import com.jvoye.tasky.core.domain.util.DataError
import com.jvoye.tasky.core.domain.util.EmptyResult
import com.jvoye.tasky.core.domain.util.Result

interface AttendeeManager {
    suspend fun fetchLookupAttendee(email: String): Result<LookupAttendee, DataError.Network>
    suspend fun removeAttendee(eventId: String): EmptyResult<DataError>
}