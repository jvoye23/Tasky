package com.jvoye.tasky.agenda.di

import com.jvoye.tasky.agenda.data.AndroidImageManager
import com.jvoye.tasky.agenda.data.OfflineFirstAgendaRepository
import com.jvoye.tasky.agenda.domain.AgendaRepository
import com.jvoye.tasky.agenda.domain.ImageManager
import com.jvoye.tasky.agenda.domain.TaskyType
import com.jvoye.tasky.agenda.presentation.agenda_details.AgendaDetailScreenViewModel
import com.jvoye.tasky.agenda.presentation.event_photo.EditPhotoScreenViewModel
import com.jvoye.tasky.core.data.StandardDispatcherProvider
import com.jvoye.tasky.core.domain.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val agendaDetailModule = module {
    singleOf(::OfflineFirstAgendaRepository).bind<AgendaRepository>()
    single<DispatcherProvider>{ StandardDispatcherProvider }
    singleOf(::AndroidImageManager).bind<ImageManager>()



    viewModel { (isEdit: Boolean, taskyType: TaskyType, taskyItemId: String? ) ->
        AgendaDetailScreenViewModel(
            isEdit = isEdit,
            taskyType = taskyType,
            taskyItemId = taskyItemId,
            agendaRepository = get(),
            savedStateHandle = get(),
            imageManager = get(),
            attendeeManager = get(),
            userValidator = get(),
            sessionStorage = get()
        )
    }

    viewModel { (localPhotoPath: String?, photoUrl: String?) ->
        EditPhotoScreenViewModel(
            localPhotoPath = localPhotoPath,
            photoUrl = photoUrl,
            agendaRepository = get()
        )
    }
}