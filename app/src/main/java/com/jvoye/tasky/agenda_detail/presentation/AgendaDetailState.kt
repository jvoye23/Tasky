package com.jvoye.tasky.agenda_detail.presentation

import com.jvoye.tasky.core.domain.model.TaskyItem
data class AgendaDetailState(
    val taskyItem: TaskyItem? = null,
    val isEdit: Boolean = false,
)