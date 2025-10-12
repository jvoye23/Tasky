package com.jvoye.tasky.core.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class FullAgendaDto(
    val events: List<EventInfoDto>,
    val tasks: List<TaskDto>,
    val reminders: List<ReminderDto>
)

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val remindAt: String,
    val isDone: Boolean
)

@Serializable
data class ReminderDto(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val remindAt: String,
)
@Serializable
data class EventDto(
    val event: EventInfoDto,
    val uploadUrls: List<UploadUrlDto>
)

@Serializable
data class EventInfoDto(
    val id: String,
    val title: String,
    val description: String,
    val from: String,
    val to: String,
    val remindAt: String? = null,
    val hostId: String,
    val isUserEventCreator: Boolean,
    val attendees: List<EventAttendeeDto>,
    val photoKeys: List<PhotoDto>
)

@Serializable
data class UploadUrlDto(
    val photoKey: String,
    val uploadKey: String,
    val url: String
)

@Serializable
data class EventAttendeeDto(
    val email: String,
    val username: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: String
)

@Serializable
data class PhotoDto(
    val key: String,
    val url: String
)
/*


{
    "event": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
    "title": "Team Meeting", (String)
    "description": "Weekly sync meeting", (String)
    "from": "2024-01-15T10:00:00Z", (String)
    "to": "2024-01-15T11:00:00Z", (String)
    "remindAt": "2024-01-15T09:30:00Z", (String)
    "host": "<Event creator ID>", (String)
    "isUserEventCreator": true, (Boolean)
    "attendees": [...attendees], (List<Attendee>)
    "photoKeys": [...downloadable photos], (List<Photo>)
},
    "uploadUrls": [
    {
        "photoKey": "photo0", (String)
        "uploadKey": "4d0daede-fbe9-449b-ae26-c4337e3515a4", (String)
        "url": "<URL that can be used to upload image>" (String)
    }
    ]
}
Attendee:
{
    "email": "<Attendee email>", (String)
    "username": "<Attendee full name>", (String)
    "userId": "<Attendee user ID>", (String)
    "eventId": "<Event the attendee belongs to>", (String)
    "isGoing": true/false, (Boolean)
    "remindAt": "2024-01-15T09:30:00Z" (String)
}

Photo:
{
"key": "<Unique key of the photo>", (String)
"url": "https://test.com/photo.png" (String)
}
*/





