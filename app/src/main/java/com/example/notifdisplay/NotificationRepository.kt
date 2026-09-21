package com.example.notifdisplay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationRepository {
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun addNotification(notification: NotificationItem) {
        _notifications.value = (listOf(notification) + _notifications.value).take(50)
    }

    fun removeNotification(id: String) {
        _notifications.value = _notifications.value.filter { it.id != id }
    }
    
    fun clearAll() {
        _notifications.value = emptyList()
    }
}
