package com.jvoye.tasky.auth.di

import com.jvoye.tasky.auth.data.AuthRepositoryImpl
import com.jvoye.tasky.auth.data.EmailPatternValidator
import com.jvoye.tasky.auth.domain.AuthRepository
import com.jvoye.tasky.auth.domain.PatternValidator
import com.jvoye.tasky.auth.domain.UserDataValidator
import com.jvoye.tasky.auth.presentation.login.LoginViewModel
import com.jvoye.tasky.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}