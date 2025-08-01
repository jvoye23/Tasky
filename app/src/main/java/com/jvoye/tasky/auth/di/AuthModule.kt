package com.jvoye.tasky.auth.di

import com.jvoye.tasky.auth.data.EmailPatternValidator
import com.jvoye.tasky.auth.domain.PatternValidator
import com.jvoye.tasky.auth.domain.UserDataValidator
import com.jvoye.tasky.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)

    viewModelOf(::RegisterViewModel)
}