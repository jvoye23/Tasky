package com.jvoye.tasky.core.data.networking.mappers

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.convertIsoStringToSystemLocalDateTime
import com.jvoye.tasky.core.data.networking.CreateEventRequest
import com.jvoye.tasky.core.data.networking.CreateReminderRequest
import com.jvoye.tasky.core.data.networking.CreateTaskRequest
import com.jvoye.tasky.core.data.networking.UpdateEventRequest
import com.jvoye.tasky.core.data.networking.dto.EventAttendeeDto
import com.jvoye.tasky.core.data.networking.dto.EventDto
import com.jvoye.tasky.core.data.networking.dto.EventInfoDto
import com.jvoye.tasky.core.data.networking.dto.FullAgendaDto
import com.jvoye.tasky.core.data.networking.dto.LookupAttendeeDto
import com.jvoye.tasky.core.data.networking.dto.PhotoDto
import com.jvoye.tasky.core.data.networking.dto.ReminderDto
import com.jvoye.tasky.core.data.networking.dto.TaskDto
import com.jvoye.tasky.core.data.networking.dto.UploadUrlDto
import com.jvoye.tasky.core.domain.model.EventAttendee
import com.jvoye.tasky.core.domain.model.EventPhoto
import com.jvoye.tasky.core.domain.model.FullAgenda
import com.jvoye.tasky.core.domain.model.LocalPhotoInfo
import com.jvoye.tasky.core.domain.model.LookupAttendee
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import com.jvoye.tasky.core.domain.model.UploadUrl
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
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


@OptIn(ExperimentalTime::class)
fun EventDto.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = event.id,
        title = event.title,
        description = event.description,
        time = convertIsoStringToSystemLocalDateTime(event.from),
        remindAt = (Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()) + 10.minutes).toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = convertIsoStringToSystemLocalDateTime(event.to),
            attendees = event.attendees.map { it.toEventAttendee() },
            photos = emptyList(),
            isUserEventCreator = event.isUserEventCreator,
            host = event.hostId,
            isGoing = event.attendees.any { it.isGoing },
        ),
    )
}

@OptIn(ExperimentalTime::class)
fun EventAttendeeDto.toEventAttendee(): EventAttendee {
    return EventAttendee(
        username = username,
        email = email,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = (Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()) + 10.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
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

fun LookupAttendeeDto.toLookupAttendee(): LookupAttendee {
    return LookupAttendee(
        userId = userId,
        email = email,
        username = fullName,
        isGoing = false // set default to false
    )
}

fun UploadUrlDto.toUploadUrl(): UploadUrl {
    return UploadUrl(
        photoKey = photoKey,
        uploadKey = uploadKey,
        url = url
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toCreateEventRequest(): CreateEventRequest {
    return CreateEventRequest(
        id = id,
        title = title,
        description = description,
        from = time.toInstant(TimeZone.UTC).toString(),
        to = (details as TaskyItemDetails.Event).toTime.toInstant(TimeZone.UTC).toString(),
        remindAt = remindAt.toInstant(TimeZone.UTC).toString(),
        attendeeIds = details.attendees.map { it.userId },
        photoKeys = details.photos.map { it.localPhotoKey },
        updatedAt = getUpdatedAtTime(),
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toUpdateEventRequest(): UpdateEventRequest {
    return UpdateEventRequest(
        title = title,
        description = description,
        from = time.toInstant(TimeZone.UTC).toString(),
        to = (details as TaskyItemDetails.Event).toTime.toInstant(TimeZone.UTC).toString(),
        remindAt = remindAt.toInstant(TimeZone.UTC).toString(),
        attendeeIds = details.attendees.map { it.userId },
        newPhotoKeys = details.newPhotosKeys,
        deletedPhotoKeys = details.deletedPhotoKeys,
        isGoing = details.isGoing,
        updatedAt = getUpdatedAtTime(),
    )
}

@OptIn(ExperimentalTime::class)
fun EventInfoDto.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id,
        title = title,
        description = description,
        time = convertIsoStringToSystemLocalDateTime(from),
        type = TaskyType.EVENT,
        remindAt = (Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault()) + 10.minutes).toLocalDateTime(TimeZone.currentSystemDefault()),
        details = TaskyItemDetails.Event(
            toTime = convertIsoStringToSystemLocalDateTime(to),
            attendees = attendees.map { it.toEventAttendee() },
            photos = photoKeys.map { it.toLocalPhotoInfo()},
            newPhotosKeys = emptyList(),
            deletedPhotoKeys = emptyList(),
            isUserEventCreator = isUserEventCreator,
            host = hostId,
            isGoing = true
        )
    )
}

fun PhotoDto.toLocalPhotoInfo(): LocalPhotoInfo {
    return LocalPhotoInfo(
        localPhotoKey = key,
        filePath = url,
        compressedBytes = byteArrayOf()
    )
}