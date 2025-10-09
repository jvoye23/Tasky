package com.jvoye.tasky.agenda.presentation.event_photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jvoye.tasky.agenda.domain.AgendaRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditPhotoScreenViewModel (
    private val localPhotoPath: String?,
    private val photoUrl: String?,
    private val agendaRepository: AgendaRepository
): ViewModel() {

    private val eventChannel = Channel<EditPhotoEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(EditPhotoState(
        localPhotoPath = localPhotoPath
    ))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                // TODO: load photo ID
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            _state.value
        )

    fun onAction(action: EditPhotoAction) {
        when(action) {
            is EditPhotoAction.OnDeleteClick -> {

            }
            EditPhotoAction.OnToggleEditMode -> {
                _state.update { it.copy(
                    isEditMode = !it.isEditMode
                )}
            }

            else -> Unit
        }
    }


}