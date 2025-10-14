package com.jvoye.tasky.agenda.di

import com.jvoye.tasky.agenda.data.KtorAttendeeManager
import com.jvoye.tasky.agenda.data.OfflineFirstAgendaRepository
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.AttendeeManager
import com.jvoye.tasky.agenda.presentation.agenda_list.AgendaViewModel
import com.jvoye.tasky.auth.data.KtorAuthRepository
import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.core.data.notification.AgendaNotificationService
import com.jvoye.tasky.core.domain.notification.NotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val agendaModule = module {
    singleOf(::KtorAuthRepository).bind<AuthRepository>()
    singleOf(::OfflineFirstAgendaRepository).bind<AgendaRepository>()
    singleOf(::KtorAttendeeManager).bind<AttendeeManager>()
    singleOf(::AgendaNotificationService).bind<NotificationService>()

    viewModelOf(::AgendaViewModel)
}