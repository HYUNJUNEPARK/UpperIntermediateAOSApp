package com.june.musicstreaming.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.june.musicstreaming.service.Constant.Companion.CHANNEL_ID
import com.june.musicstreaming.service.Constant.Companion.CHANNEL_NAME
import com.june.musicstreaming.service.Constant.Companion.NOTIFICATION_ID
import com.june.musicstreaming.service.Constant.Companion.NOTIFICATION_TITLE
import com.june.musicstreaming.service.Constant.Companion.PLAYER_INTENT_ACTION

class Notification(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    lateinit var builder: NotificationCompat.Builder

    fun notifyNotification() {
        notification()
        notificationManager.notify(
            NOTIFICATION_ID,
            builder.build()
        )
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun notification() {
        val intent = Intent(context, ForegroundService::class.java)
        intent.action = PLAYER_INTENT_ACTION

        builder = notificationBuilder().apply {
            setSmallIcon(android.R.drawable.ic_notification_overlay)
            setContentTitle(NOTIFICATION_TITLE)
            //User can not cancel notification
            setAutoCancel(false) //block touch cancel
            setOngoing(true) //block swipe cancel
        }
    }

    private fun notificationBuilder(): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(context, CHANNEL_ID)
            return builder
        }
        else {
            builder = NotificationCompat.Builder(context)
            return builder
        }
    }
}