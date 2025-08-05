package com.jvoye.tasky.core.data.di

import com.jvoye.tasky.core.data.networking.HttpClientFactory
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
}