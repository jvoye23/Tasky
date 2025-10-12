package com.jvoye.tasky.core.domain.model

/**
 * Represents the Domain model for a LookupAttendee
 */
data class LookupAttendee(
    override val userId: String,
    override val email: String,
    override val username: String,
    override val isGoing: Boolean
): AttendeeBase
