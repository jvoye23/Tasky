package com.jvoye.tasky.core.data.di

import com.jvoye.tasky.core.data.auth.EncryptedSessionDataStore
import com.jvoye.tasky.core.data.networking.HttpClientFactory
import com.jvoye.tasky.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionDataStore).bind<SessionStorage>()
}