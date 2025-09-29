package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class CreateReminderRequest(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val remindAt: String,
    val updatedAt: String
)


/*POST REMINDER
{
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
    "title": "Doctor Appointment", (String)
    "description": "Annual checkup", (String)
    "time": "2024-01-15T16:00:00Z", (String)
    "remindAt": "2024-01-15T15:30:00Z", (String)
    "updatedAt": "2024-01-15T15:00:00Z" (String)
}*/
