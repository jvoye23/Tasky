package com.jvoye.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskRequest(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val remindAt: String,
    val updatedAt: String,
    val isDone: Boolean
)


