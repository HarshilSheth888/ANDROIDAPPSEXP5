package com.example.notifdisplay

import android.content.Context
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotificationViewModel : ViewModel() {
    
    val notifications: StateFlow<List<NotificationItem>> = NotificationRepository.notifications
    
    val isEmpty: StateFlow<Boolean> = notifications
        .map { it.isEmpty() }
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun isNotificationServiceEnabled(context: Context): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(context)
        return packageNames.contains(context.packageName)
    }

    fun clearAll() {
        NotificationRepository.clearAll()
    }
}
