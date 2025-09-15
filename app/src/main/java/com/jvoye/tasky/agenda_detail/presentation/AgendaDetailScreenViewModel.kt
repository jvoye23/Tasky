package com.jvoye.tasky.agenda_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.core.domain.model.TaskyNavParcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AgendaDetailScreenViewModel(
    private val isEdit: Boolean,
    private val taskyType: TaskyType,
    private val taskyItemId: Long?,
    private val agendaRepository: AgendaRepository,
    private val savedStateHandle: SavedStateHandle


): ViewModel() {

    private val _state = MutableStateFlow(AgendaDetailState(
        isEdit = isEdit,
    ))
    private var hasLoadedInitialData = false

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getTaskyItem(taskyItemId)
                getNavArgs()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    private suspend fun getTaskyItem(taskyItemId: Long?) {
        if (taskyItemId == null) return
        _state.update { it.copy(
            taskyItem = agendaRepository.getTaskyItem(taskyItemId)
        ) }
    }

    private fun getNavArgs() {
        val savedState =  savedStateHandle.get<TaskyNavParcelable>("TaskyNavArgs")

        _state.update { it.copy(
            taskyItemId = savedState?.taskyItemId,
            taskyItemType = savedState?.taskyItemType ?: TaskyType.TASK,
            isEdit = savedState?.isEditMode ?: true

        ) }

        // prints null
        println("SAVED STATE 2: $savedState")
    }
}

/*
// Updating a shared property
state = state.copy(title = "new title")

// Updating an individual property
state = state.copy(
details = detailsAsEvent()?.copy(attendees = newAttendees)
)
*/