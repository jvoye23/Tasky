package com.jvoye.tasky.agenda.presentation.agenda_details.mappers

import com.jvoye.tasky.core.domain.model.Attendee
import com.jvoye.tasky.core.domain.model.User
import kotlinx.datetime.LocalDateTime

// The eventId value and remindAt value will be updated before sending the create event request
fun User.toAttendee(): Attendee {
    return Attendee(
        userId = userId,
        email = email,
        username = fullName,
        eventId = "",
        isGoing = false,
        remindAt = LocalDateTime(2023, 1, 1, 12, 0)
    )
}