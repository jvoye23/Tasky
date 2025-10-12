package com.jvoye.tasky.core.data

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.domain.NotificationType
import com.jvoye.tasky.core.domain.model.EventAttendee
import com.jvoye.tasky.core.domain.model.LocalPhotoInfo
import com.jvoye.tasky.core.domain.model.TaskyItem
import com.jvoye.tasky.core.domain.model.TaskyItemDetails
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val testTaskyItems = mutableListOf<TaskyItem>(
    TaskyItem(
        id = "1",
        title = "Task 1 Title",
        description = "Task 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = false
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "2",
        title = "Task 2 Title",
        description = "Task 2 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = true
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "3",
        title = "Event 1 Title",
        description = "Event 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            eventAttendees = listOf<EventAttendee>(
                EventAttendee(
                    email = "test1@email.com",
                    username = "Attendee 1",
                    userId = "1",
                    eventId = "2",
                    isGoing = true,
                    remindAt = "2025-10-12T16:50:00Z"
                )
            ),
            photos = listOf(
                LocalPhotoInfo(
                    localPhotoKey = "123",
                    filePath = "uriString",
                    compressedBytes = ByteArray(0)
                )
            ),
            isUserEventCreator = true,
            host = "Host 1"
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "4",
        title = "Event 2 Title",
        description = "Event 2 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            eventAttendees = listOf<EventAttendee>(
                EventAttendee(
                    email = "test2@email.com",
                    username = "Attendee 2",
                    userId = "2",
                    eventId = "2",
                    isGoing = true,
                    remindAt = "2025-10-12T16:50:00Z"
                )
            ),
            photos = listOf(
                LocalPhotoInfo(
                    localPhotoKey = "123",
                    filePath = "uriString",
                    compressedBytes = ByteArray(0)
                )
            ),
            isUserEventCreator = true,
            host = "Host 2"
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "5",
        title = "Reminder 1 Title",
        description = "Reminder 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "6",
        title = "Reminder 2 Title",
        description = "Reminder 2 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "7",
        title = "Task 3 Title",
        description = "Task 3 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = false
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "8",
        title = "Task 4 Title",
        description = "Task 4 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = true
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "9",
        title = "Event 3 Title",
        description = "Event 3 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            eventAttendees = listOf<EventAttendee>(
                EventAttendee(
                    email = "test3@email.com",
                    username = "Attendee 3",
                    userId = "3",
                    eventId = "3",
                    isGoing = true,
                    remindAt = "2025-10-12T16:50:00Z"
                )
            ),
            photos = listOf(
                LocalPhotoInfo(
                    localPhotoKey = "123",
                    filePath = "uriString",
                    compressedBytes = ByteArray(0)
                )
            ),
            isUserEventCreator = true,
            host = "Host 1"
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "10",
        title = "Event 4 Title",
        description = "Event 4 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            eventAttendees = listOf<EventAttendee>(
                EventAttendee(
                    email = "test4@email.com",
                    username = "Attendee 4",
                    userId = "4",
                    eventId = "4",
                    isGoing = true,
                    remindAt = "2025-10-12T16:50:00Z"
                )
            ),
            photos = listOf(
                LocalPhotoInfo(
                    localPhotoKey = "123",
                    filePath = "uriString",
                    compressedBytes = ByteArray(0)
                )
            ),
            isUserEventCreator = true,
            host = "Host 4"
        ),
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "11",
        title = "Reminder 3 Title",
        description = "Reminder 3 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = "12",
        title = "Reminder 4 Title",
        description = "Reminder 4 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        remindAt = LocalDateTime(2025, 11, 1, 10, 0),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
)