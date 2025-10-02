package com.jvoye.tasky.agenda.di

import com.jvoye.tasky.agenda.data.OfflineFirstAgendaRepository
import com.jvoye.tasky.agenda.data.TestAgendaRepository
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.MockAgendaRepository
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailScreenViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaDetailModule = module {

    singleOf(::TestAgendaRepository).bind<MockAgendaRepository>()
    singleOf(::OfflineFirstAgendaRepository).bind<AgendaRepository>()

    viewModel { (isEdit: Boolean, taskyType: TaskyType, taskyItemId: String? ) ->
        AgendaDetailScreenViewModel(
            isEdit = isEdit,
            taskyType = taskyType,
            taskyItemId = taskyItemId,
            agendaRepository = get(),
            savedStateHandle = get()
        )
    }
}