package com.jvoye.tasky.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Attendee(
    val username: String,
    val email: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: LocalDateTime

)
interface AttendeeBase {
    val userId: String
    val email: String
    val name: String
    val isGoing: Boolean
}