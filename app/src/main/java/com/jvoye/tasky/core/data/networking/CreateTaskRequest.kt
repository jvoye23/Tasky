package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskRequest(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val remindAt: String,
    val updatedAt: String,
    val isDone: Boolean
)



/*POST TASK
{
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
    "title": "Complete API documentation", (String)
    "description": "Write comprehensive API docs", (String)
    "time": "2024-01-15T14:00:00Z", (String)
    "remindAt": "2024-01-15T13:45:00Z", (String)
    "updatedAt": "2024-01-15T12:00:00Z", (String)
    "isDone": false (Boolean)
}*/


