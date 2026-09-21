package com.example.notifdisplay

import android.graphics.drawable.Icon

data class NotificationItem(
    val id: String,
    val packageName: String,
    val appName: String,
    val title: String?,
    val text: String?,
    val icon: Icon?,
    val postTime: Long,
    val isClearable: Boolean
)
