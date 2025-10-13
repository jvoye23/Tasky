package com.jvoye.tasky.core.domain.model


/**
 * Represents the Domain model for a LookupAttendee
 */
data class EventAttendee (
    override val userId: String,
    override val email: String,
    override val username: String,
    override val isGoing: Boolean,
    val eventId: String,
    val remindAt: String,
): AttendeeBase
