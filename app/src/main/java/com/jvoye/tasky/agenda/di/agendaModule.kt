package com.jvoye.tasky.agenda.di

import com.jvoye.tasky.agenda.data.OfflineFirstAgendaRepository
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.presentation.agenda_list.AgendaViewModel
import com.jvoye.tasky.auth.data.KtorAuthRepository
import com.jvoye.tasky.auth.domain.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaModule = module {
    singleOf(::KtorAuthRepository).bind<AuthRepository>()
    singleOf(::OfflineFirstAgendaRepository).bind<AgendaRepository>()

    viewModelOf(::AgendaViewModel)
}