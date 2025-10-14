package com.jvoye.tasky.agenda.domain

import kotlinx.serialization.Serializable

@Serializable
enum class TaskyType {
    EVENT,
    TASK,
    REMINDER
}