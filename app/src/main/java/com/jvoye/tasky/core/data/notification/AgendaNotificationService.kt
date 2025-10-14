@file:OptIn(ExperimentalTime::class)

package com.jvoye.tasky.core.data.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.jvoye.tasky.core.domain.model.AgendaNotification
import com.jvoye.tasky.core.domain.notification.NotificationService
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AgendaNotificationService(
    private val context: Context
): NotificationService {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleNotification(notification: AgendaNotification) {
        val currentTime = Clock.System.now()
        if(notification.remindAt != null && notification.remindAt <= currentTime) {
            return
        }
        val intent = Intent(context, AgendaNotificationReceiver::class.java)
        intent.putExtra("NOTIFICATION_ID", notification.notificationId)
        intent.putExtra("NOTIFICATION_TITLE", notification.title)
        intent.putExtra("NOTIFICATION_DESCRIPTION", notification.description?: "")
        intent.putExtra("NOTIFICATION_TASKY_TYPE", notification.notificationTaskyType)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.notificationId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            notification.remindAt?.toEpochMilliseconds() ?: 0,
            pendingIntent
        )
    }

    override fun cancelNotification(notificationId: String) {
        val intent = Intent(context, AgendaNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.let { pendingIntent ->
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    override fun cancelAllNotifications(notificationIds: List<String>) {
        notificationIds.forEach { notificationId ->
            val intent = Intent(context, AgendaNotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            pendingIntent?.let { pendingIntent ->
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }
}