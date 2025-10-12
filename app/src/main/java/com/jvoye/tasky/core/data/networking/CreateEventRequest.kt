package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

/**
 * POST /event
 * Content-Type: application/json
 * Authorization: Bearer <token>
 * {
 *  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
 *  "title": "Team Meeting", (String)
 *  "description": "Weekly sync meeting", (String)
 *  "from": "2024-01-15T10:00:00Z", (String)
 *  "to": "2024-01-15T11:00:00Z", (String)
 *  "remindAt": "2024-01-15T09:30:00Z", (String)
 *  "attendeeIds": ["507f1f77bcf86cd799439012"], (List<String>)
 *  "photoKeys": ["photo01", "photo02"], (List<String>)
 *  "updatedAt": "2024-01-15T09:00:00Z" (String)
 * }
 */


@Serializable
data class CreateEventRequest(
    val id: String,
    val title: String,
    val description: String,
    val from: String,
    val to: String,
    val remindAt: String,
    val attendeeIds: List<String>,
    val photoKeys: List<String>,
    val updatedAt: String
)