package com.example.notifdisplay

import android.app.Notification
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        val packageName = sbn.packageName
        
        val appName = try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }

        val item = NotificationItem(
            id = sbn.key,
            packageName = packageName,
            appName = appName,
            title = title,
            text = text,
            icon = sbn.notification.smallIcon,
            postTime = sbn.postTime,
            isClearable = sbn.isClearable
        )

        NotificationRepository.addNotification(item)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        NotificationRepository.removeNotification(sbn.key)
    }

    override fun onListenerConnected() {
        Log.d("NotificationListener", "Connected")
        val activeNotifications = activeNotifications
        activeNotifications?.forEach { onNotificationPosted(it) }
    }
}
