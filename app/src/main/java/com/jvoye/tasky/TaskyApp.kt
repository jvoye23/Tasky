package com.jvoye.tasky

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.jvoye.tasky.agenda.di.agendaModule
import com.jvoye.tasky.agenda.di.agendaDetailModule
import com.jvoye.tasky.auth.di.authModule
import com.jvoye.tasky.core.data.di.coreDataModule
import com.jvoye.tasky.core.data.networking.di.networkModule
import com.jvoye.tasky.core.data.notification.AgendaNotificationReceiver.Companion.NOTIFICATION_CHANNEL_ID
import com.jvoye.tasky.core.database.di.databaseModule
import com.jvoye.tasky.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TaskyApp: Application() {
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@TaskyApp)
            modules(
                appModule,
                authModule,
                coreDataModule,
                agendaModule,
                agendaDetailModule,
                databaseModule,
                networkModule
            )
        }
        createNotificationChannel()

    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Tasky reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for agenda items"
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}