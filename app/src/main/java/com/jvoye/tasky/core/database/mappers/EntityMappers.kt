@file:OptIn(ExperimentalUuidApi::class)

package com.jvoye.tasky.core.database.mappers

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.database.entity.EventEntity
import com.jvoye.tasky.core.database.entity.ReminderEntity
import com.jvoye.tasky.core.database.entity.TaskEntity
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun TaskEntity.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id.toString(),
        title = title,
        description = description,
        type = TaskyType.TASK,
        time = LocalDateTime.parse(dateTimeUtc),
        details = TaskyItemDetails.Task(
            isDone = isDone
        ),
        remindAt = LocalDateTime.parse(remindAt)
    )
}

fun TaskyItem.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id.toUuid(),
        type = type.toString(),
        title = title,
        description = description,
        dateTimeUtc = time.toString(),
        isDone = (details as TaskyItemDetails.Task).isDone,
        remindAt = remindAt.toString()
    )
}

fun ReminderEntity.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id.toString(),
        title = title,
        description = description,
        type = TaskyType.REMINDER,
        time = LocalDateTime.parse(dateTimeUtc),
        remindAt = LocalDateTime.parse(remindAt),
        details = TaskyItemDetails.Reminder,
    )
}

fun TaskyItem.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id.toUuid(),
        type = type.toString(),
        title = title,
        description = description,
        dateTimeUtc = time.toString(),
        remindAt = remindAt.toString()
    )
}

fun EventEntity.toTaskyItem(): TaskyItem {
    return TaskyItem(
        id = id.toString(),
        title = title,
        description = description,
        type = TaskyType.EVENT,
        time = LocalDateTime.parse(dateTimeUtc),
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime.parse(toDateTimeUtc),
            attendees = attendees,
            photos = photos,
            isUserEventCreator = isUserEventCreator,
            host = host
        ),
        remindAt = LocalDateTime.parse(remindAt)
    )
}

fun TaskyItem.toEventEntity(): EventEntity {
    return EventEntity(
        id = id.toUuid(),
        type = type.toString(),
        title = title,
        description = description,
        dateTimeUtc = time.toString(),
        toDateTimeUtc = (details as TaskyItemDetails.Event).toTime.toString(),
        attendees = details.attendees,
        photos = details.photos,
        remindAt = remindAt.toString(),
        isUserEventCreator = details.isUserEventCreator,
        host = details.host
    )
}

fun String.toUuid(): Uuid {
    return Uuid.parse(this)
}
