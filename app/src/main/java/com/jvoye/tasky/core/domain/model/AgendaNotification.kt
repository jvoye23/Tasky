@file:OptIn(ExperimentalTime::class)

package com.jvoye.tasky.core.domain.model

import com.jvoye.tasky.agenda.domain.TaskyType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class AgendaNotification(
    val notificationId: String,
    val title: String,
    val description: String?,
    val remindAt: Instant?,
    val notificationTaskyType: TaskyType,
)

fun TaskyItem.toAgendaNotification(): AgendaNotification {
    return AgendaNotification(
        notificationId = id,
        title = title,
        description = description,
        remindAt = remindAt,
        notificationTaskyType = type
    )
}