package com.jvoye.tasky.di

import com.jvoye.tasky.MainViewModel
import com.jvoye.tasky.TaskyApp
import com.jvoye.tasky.auth.presentation.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as TaskyApp).applicationScope
    }

    viewModelOf(::MainViewModel)
}