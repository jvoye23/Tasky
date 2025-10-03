package com.jvoye.tasky.core.data.networking.mappers

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.convertIsoStringToSystemLocalDateTime
import com.jvoye.tasky.core.data.networking.AttendeeDto
import com.jvoye.tasky.core.data.networking.CreateReminderRequest
import com.jvoye.tasky.core.data.networking.CreateTaskRequest
import com.jvoye.tasky.core.data.networking.EventDto
import com.jvoye.tasky.core.data.networking.FullAgendaDto
import com.jvoye.tasky.core.data.networking.PhotoDto
import com.jvoye.tasky.core.data.networking.ReminderDto
import com.jvoye.tasky.core.data.networking.TaskDto
import com.jvoye.tasky.core.domain.model.Attendee
import com.jvoye.tasky.core.domain.model.EventPhoto
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.UUID.randomUUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/*POST TASK
{
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
    "title": "Complete API documentation", (String)
    "description": "Write comprehensive API docs", (String)
    "time": "2024-01-15T14:00:00Z", (String)
    "remindAt": "2024-01-15T13:45:00Z", (String)
    "updatedAt": "2024-01-15T12:00:00Z", (String)
    "isDone": false (Boolean)
}
RESPONSE TASK
{
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
    "title": "Complete API documentation", (String)
    "description": "Write comprehensive API docs", (String)
    "time": "2024-01-15T14:00:00Z", (String)
    "remindAt": "2024-01-15T13:45:00Z", (String)
    "isDone": true (Boolean)
}

RESPONSE REMINDER
{
 "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479", (String)
 "title": "Doctor Appointment", (String)
 "description": "Annual checkup", (String)
 "time": "2024-01-15T16:00:00Z", (String)
 "remindAt": "2024-01-15T15:30:00Z" (String)
}

RESPONSE FULL AGENDA
{
"events": [...events], (List<Event>)
"tasks": [...tasks], (List<Task>)
"reminders": [...reminders] (List<Reminder>)
}
*/



fun TaskDto.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id,
        title = title,
        description = description,
        time = convertIsoStringToSystemLocalDateTime(time),
        remindAt = convertIsoStringToSystemLocalDateTime(remindAt),
        details = TaskyItemDetails.Task(
            isDone = isDone
        ),
        type = TaskyType.TASK,
    )
}


fun ReminderDto.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id,
        title = title,
        description = description,
        time = convertIsoStringToSystemLocalDateTime(time),
        remindAt = convertIsoStringToSystemLocalDateTime(remindAt),
        details = TaskyItemDetails.Reminder,
        type = TaskyType.REMINDER
    )
}


fun EventDto.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = event.id,
        title = event.title,
        description = event.description,
        time = convertIsoStringToSystemLocalDateTime(event.from),
        remindAt = convertIsoStringToSystemLocalDateTime(event.remindAt),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = convertIsoStringToSystemLocalDateTime(event.to),
            attendees = event.attendees.map { it.toAttendee() },
            photos = event.photoKeys.map { it.toEventPhoto() },
            isUserEventCreator = event.isUserEventCreator,
            host = event.host
        ),
    )
}

fun AttendeeDto.toAttendee(): Attendee {
    return Attendee(
        username = username,
        email = email,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = LocalDateTime.parse(remindAt)
    )
}

fun PhotoDto.toEventPhoto(): EventPhoto {
    return EventPhoto(
        key = key,
        url = url
    )
}


fun FullAgendaDto.toFullAgenda(): FullAgenda {
    return FullAgenda(
        events = events.map { it.toTaskyItem() },
        tasks = tasks.map { it.toTaskyItem() },
        reminders = reminders.map { it.toTaskyItem() }
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toCreateTaskRequest(): CreateTaskRequest {
    return CreateTaskRequest(
        id = id,
        title = title,
        description = description,
        time = time.toInstant(TimeZone.UTC).toString(),
        remindAt = remindAt.toInstant(TimeZone.UTC).toString(),
        updatedAt = getUpdatedAtTime(),
        isDone = (details as TaskyItemDetails.Task).isDone
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toCreateReminderRequest(): CreateReminderRequest {
    return CreateReminderRequest(
        id = id,
        title = title,
        description = description,
        time = time.toInstant(TimeZone.UTC).toString(),
        remindAt = remindAt.toInstant(TimeZone.UTC).toString(),
        updatedAt = getUpdatedAtTime()
    )
}

@OptIn(ExperimentalTime::class)
private fun getUpdatedAtTime(): String {
    val now = Clock.System.now()
    val truncatedInstant = Instant.fromEpochSeconds(now.epochSeconds)
    val isoString = truncatedInstant.toString()
    return isoString
}

