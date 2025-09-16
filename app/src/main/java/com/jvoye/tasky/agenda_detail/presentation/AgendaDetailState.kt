package com.jvoye.tasky.agenda_detail.presentation

import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.TaskyItem
import kotlin.time.ExperimentalTime

data class AgendaDetailState @OptIn(ExperimentalTime::class) constructor(
    /*val taskyItem: TaskyItem = TaskyItem(
        id = 0,
        type = TaskyType.TASK,
        title = "Title",
        description = "Description",
        time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        details = TaskyItemDetails.Task(isDone = false)
    ),*/
    val taskyItem: TaskyItem? = null,
    val taskyItemId: Long? = null,
    val isEdit: Boolean = false,
    val taskyItemType: TaskyType = TaskyType.TASK
)