package com.jvoye.tasky.core.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Represents the Domain model for a LookupAttendee
 */
data class EventAttendee @OptIn(ExperimentalTime::class) constructor(
    override val userId: String,
    override val email: String,
    override val name: String,
    override val isGoing: Boolean,
    val eventId: String,
    val remindAt: LocalDateTime,
): AttendeeBase
