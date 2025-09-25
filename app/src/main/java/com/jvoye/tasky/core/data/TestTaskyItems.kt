package com.jvoye.tasky.core.data

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.domain.NotificationType
import com.jvoye.tasky.core.domain.model.Attendee
import com.jvoye.tasky.core.domain.model.EventPhoto
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
        id = 1,
        title = "Task 1 Title",
        description = "Task 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = false
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 2,
        title = "Task 2 Title",
        description = "Task 2 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = true
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 3,
        title = "Event 1 Title",
        description = "Event 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            attendees = listOf<Attendee>(
                Attendee(
                    name = "Attendee 1",
                    email = "test1@email.com"
                )
            ),
            photos = listOf(
                EventPhoto(
                    id = 1
                )
            )
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 4,
        title = "Event 2 Title",
        description = "Event 2 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            attendees = listOf<Attendee>(
                Attendee(
                    name = "Attendee 2",
                    email = "test2@email.com"
                )
            ),
            photos = listOf(
                EventPhoto(
                    id = 2
                )
            )
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 5,
        title = "Reminder 1 Title",
        description = "Reminder 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 6,
        title = "Reminder 2 Title",
        description = "Reminder 2 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 7,
        title = "Task 3 Title",
        description = "Task 3 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = false
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 8,
        title = "Task 4 Title",
        description = "Task 4 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.TASK,
        details = TaskyItemDetails.Task(
            isDone = true
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 9,
        title = "Event 3 Title",
        description = "Event 1 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            attendees = listOf<Attendee>(
                Attendee(
                    name = "Attendee 3",
                    email = "test3@email.com"
                )
            ),
            photos = listOf(
                EventPhoto(
                    id = 3
                )
            )
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 10,
        title = "Event 4 Title",
        description = "Event 4 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.EVENT,
        details = TaskyItemDetails.Event(
            toTime = LocalDateTime(2023, 3, 1, 10, 0),
            attendees = listOf<Attendee>(
                Attendee(
                    name = "Attendee 4",
                    email = "test4@email.com"
                )
            ),
            photos = listOf(
                EventPhoto(
                    id = 4
                )
            )
        ),
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 11,
        title = "Reminder 3 Title",
        description = "Reminder 3 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
    TaskyItem(
        id = 12,
        title = "Reminder 4 Title",
        description = "Reminder 4 description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        type = TaskyType.REMINDER,
        details = TaskyItemDetails.Reminder,
        notificationType = NotificationType.THIRTY_MINUTES_BEFORE
    ),
)