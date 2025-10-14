package com.jvoye.tasky.core.data.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jvoye.tasky.MainActivity
import android.app.TaskStackBuilder
import android.os.Build
import androidx.core.app.NotificationCompat
import com.jvoye.tasky.R
import com.jvoye.tasky.agenda.domain.TaskyType

class AgendaNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationId = intent?.getStringExtra("NOTIFICATION_ID") ?: return
        val notificationTitle = intent.getStringExtra("NOTIFICATION_TITLE") ?: return
        val notificationDescription = intent.getStringExtra("NOTIFICATION_DESCRIPTION") ?: return
        val notificationTaskyType = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("NOTIFICATION_TASKY_TYPE") as? TaskyType
        } else {
            intent.getSerializableExtra("NOTIFICATION_TASKY_TYPE", TaskyType::class.java)
        }

        if(notificationTaskyType != null) {
            showNotification(
                context = context,
                notificationId = notificationId,
                notificationTitle = notificationTitle,
                notificationDescription = notificationDescription,
                notificationTaskyType = notificationTaskyType
            )
        }
    }

    private fun showNotification(
        context: Context,
        notificationId: String,
        notificationTitle: String,
        notificationDescription: String,
        notificationTaskyType: TaskyType
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create an Intent to launch MainActivity
        val detailIntent = Intent(context, MainActivity::class.java).apply {
            action = "NAVIGATE_TO_AGENDA_DETAIL" // Custom action to identify this intent
            putExtra("TASKY_ITEM_ID", notificationId)
            putExtra("TASKY_TYPE", notificationTaskyType)
            // These flags are key for correct navigation behavior
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId.hashCode(),
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_tasky_icon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationDescription)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId.hashCode(), notification)
    }
    companion object Companion {
        const val NOTIFICATION_CHANNEL_ID = "tasky_notification_channel"
    }

}
