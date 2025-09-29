package com.jvoye.tasky.core.database.di

import androidx.room.Room
import com.jvoye.tasky.core.database.RoomLocalTaskyItemDataSource
import com.jvoye.tasky.core.database.TaskyDatabase
import com.jvoye.tasky.core.domain.LocalTaskyItemDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            TaskyDatabase::class.java,
            "tasky.db"
        ).build()
    }
    single { get<TaskyDatabase>().taskDao }
    single { get<TaskyDatabase>().eventDao }
    single { get<TaskyDatabase>().reminderDao }

    singleOf(::RoomLocalTaskyItemDataSource).bind<LocalTaskyItemDataSource>()
}