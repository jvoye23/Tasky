package com.jvoye.tasky.core.domain.model

data class FullAgenda(
    val events: List<TaskyItem>,
    val tasks: List<TaskyItem>,
    val reminders: List<TaskyItem>
)
