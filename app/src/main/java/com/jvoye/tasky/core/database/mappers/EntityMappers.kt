package com.jvoye.tasky.core.database.mappers

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.mappers.convertIsoStringToSystemLocalDateTime
import com.jvoye.tasky.core.database.entity.EventEntity
import com.jvoye.tasky.core.database.entity.ReminderEntity
import com.jvoye.tasky.core.database.entity.TaskEntity
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime

fun TaskEntity.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id,
        title = title,
        description = description,
        type = TaskyType.TASK,
        time = convertIsoStringToSystemLocalDateTime(dateTimeUtc),
        details = TaskyItemDetails.Task(
            isDone = isDone
        ),
        remindAt = convertIsoStringToSystemLocalDateTime(remindAtUtc)
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        type = type.toString(),
        title = title,
        description = description,
        dateTimeUtc = time.toInstant(TimeZone.UTC).toString(),
        isDone = (details as TaskyItemDetails.Task).isDone,
        remindAtUtc = remindAt.toInstant(TimeZone.UTC).toString()
    )
}

fun ReminderEntity.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id,
        title = title,
        description = description,
        type = TaskyType.REMINDER,
        time = convertIsoStringToSystemLocalDateTime(dateTimeUtc),
        remindAt = convertIsoStringToSystemLocalDateTime(remindAtUtc),
        details = TaskyItemDetails.Reminder,
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        type = type.toString(),
        title = title,
        description = description,
        dateTimeUtc = time.toInstant(TimeZone.UTC).toString(),
        remindAtUtc = remindAt.toInstant(TimeZone.UTC).toString()
    )
}

fun EventEntity.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id,
        title = title,
        description = description,
        type = TaskyType.EVENT,
        time = convertIsoStringToSystemLocalDateTime(dateTimeUtc),
        details = TaskyItemDetails.Event(
            toTime = convertIsoStringToSystemLocalDateTime(toDateTimeUtc),
            eventAttendees = attendees,
            photos = emptyList(),
            isUserEventCreator = isUserEventCreator,
            host = host,
            remotePhotos = remotePhotos
        ),
        remindAt = convertIsoStringToSystemLocalDateTime(remindAtUtc)
    )
}

@OptIn(ExperimentalTime::class)
fun TaskyItem.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        type = type.toString(),
        title = title,
        description = description,
        dateTimeUtc = time.toInstant(TimeZone.UTC).toString(),
        toDateTimeUtc = (details as TaskyItemDetails.Event).toTime.toInstant(TimeZone.UTC).toString(),
        attendees = details.eventAttendees,
        remotePhotos = details.remotePhotos,
        remindAtUtc = remindAt.toInstant(TimeZone.UTC).toString(),
        isUserEventCreator = details.isUserEventCreator,
        host = details.host
    )
}
