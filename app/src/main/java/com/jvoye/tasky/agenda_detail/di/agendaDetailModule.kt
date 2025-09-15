package com.jvoye.tasky.agenda_detail.di

import androidx.lifecycle.SavedStateHandle
import com.jvoye.tasky.agenda.data.TestAgendaRepository
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaDetailModule = module {

    singleOf(::TestAgendaRepository).bind<AgendaRepository>()

    viewModel { (isEdit: Boolean, taskyType: TaskyType, taskyItemId: Long?, savedStateHandle: SavedStateHandle) ->
        AgendaDetailScreenViewModel(
            isEdit = isEdit,
            taskyType = taskyType,
            taskyItemId = taskyItemId,
            agendaRepository = get(),
            savedStateHandle = savedStateHandle
        )
    }
}