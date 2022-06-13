package com.june.musicstreaming.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.june.musicstreaming.R
import com.june.musicstreaming.service.Constant.Companion.CHANNEL_ID
import com.june.musicstreaming.service.Constant.Companion.CHANNEL_NAME
import com.june.musicstreaming.service.Constant.Companion.NOTIFICATION_ID
import com.june.musicstreaming.service.Constant.Companion.PLAYER_INTENT_ACTION

class Notification(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    lateinit var builder: NotificationCompat.Builder

    fun notifyNotification(artist: String, title: String) {
        notification(artist, title)
        notificationManager.notify(
            NOTIFICATION_ID,
            builder.build()
        )
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun notification(artist: String, title: String) {
        val intent = Intent(context, ForegroundService::class.java)
        intent.action = PLAYER_INTENT_ACTION

        val contentView = RemoteViews(context.packageName, R.layout.notification)
        contentView.setImageViewResource(R.id.coverImage, R.mipmap.ic_launcher)
        contentView.setTextViewText(R.id.title, "$title")
        contentView.setTextViewText(R.id.artist, "$artist")

        builder = notificationBuilder().apply {
            setSmallIcon(android.R.drawable.ic_notification_overlay)
            //User can not cancel notification
            setAutoCancel(false) //block touch cancel
            setOngoing(true) //block swipe cancel
            //custom notification
            setContent(contentView)
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