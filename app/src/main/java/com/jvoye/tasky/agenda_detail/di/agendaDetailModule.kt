package com.jvoye.tasky.agenda_detail.di

import com.jvoye.tasky.agenda_detail.presentation.AgendaDetailScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agendaDetailModule = module {
    viewModelOf(::AgendaDetailScreenViewModel)
}