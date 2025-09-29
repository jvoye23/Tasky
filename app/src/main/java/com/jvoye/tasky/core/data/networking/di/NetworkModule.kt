package com.jvoye.tasky.core.data.networking.di

import com.jvoye.tasky.core.data.networking.KtorRemoteTaskyItemDataSource
import com.jvoye.tasky.core.domain.RemoteTaskyItemDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorRemoteTaskyItemDataSource).bind<RemoteTaskyItemDataSource>()

}