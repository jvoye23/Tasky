package com.jvoye.tasky.agenda_detail.presentation

import androidx.lifecycle.ViewModel
import com.jvoye.tasky.agenda.domain.AgendaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AgendaDetailScreenViewModel(
    private val agendaType: AgendaType
): ViewModel() {

    private val _state = MutableStateFlow(
        AgendaDetailState(
            agendaTypeName = agendaType.name
        ))
    val state = _state.asStateFlow()




}