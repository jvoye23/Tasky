package com.jvoye.tasky.core.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class LookupAttendeeDto(
    val email: String,
    val fullName: String,
    val userId: String
)