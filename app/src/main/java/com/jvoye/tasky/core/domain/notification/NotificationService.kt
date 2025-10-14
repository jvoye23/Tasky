package com.jvoye.tasky.core.domain.notification

import com.jvoye.tasky.core.domain.model.AgendaNotification

interface NotificationService {
    fun scheduleNotification(notification: AgendaNotification)
    fun cancelNotification(notificationId: String)
    fun cancelAllNotifications(notificationIds: List<String>)
}