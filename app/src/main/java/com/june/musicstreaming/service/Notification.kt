package com.june.musicstreaming.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.june.musicstreaming.R
import com.june.musicstreaming.exoPlayer.ExoPlayer.Companion.player
import com.june.musicstreaming.service.Constant.Companion.CHANNEL_ID
import com.june.musicstreaming.service.Constant.Companion.CHANNEL_NAME
import com.june.musicstreaming.service.Constant.Companion.NOTIFICATION_ID
import com.june.musicstreaming.service.Constant.Companion.PLAYER_INTENT_ACTION
import com.june.musicstreaming.service.Constant.Companion.PLAY_CONTROL
import com.june.musicstreaming.service.Constant.Companion.PLAY_CONTROL_REQ_CODE
import com.june.musicstreaming.service.Constant.Companion.SKIP_NEXT
import com.june.musicstreaming.service.Constant.Companion.SKIP_NEXT_REQ_CODE
import com.june.musicstreaming.service.Constant.Companion.SKIP_PREV
import com.june.musicstreaming.service.Constant.Companion.SKIP_PREV_REQ_CODE

class Notification(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    lateinit var builder: NotificationCompat.Builder
    lateinit var mContentView: RemoteViews

    fun notifyNotification(artist: String, title: String, coverURL:String) {
        notification(artist, title, coverURL)
        notificationManager.notify(
            NOTIFICATION_ID,
            builder.build()
        )
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun notification(artist: String, title: String, coverURL:String) {
        val intent = Intent(context, ForegroundService::class.java)
        intent.action = PLAYER_INTENT_ACTION

        //notification UI
        notificationUI(artist, title, coverURL)

        builder = notificationBuilder().apply {
            setSmallIcon(android.R.drawable.ic_notification_overlay)
            //User can not cancel notification
            setAutoCancel(false) //block touch cancel
            setOngoing(true) //block swipe cancel
            //custom notification
            setContent(mContentView)
        }
        skipPrevImageViewClicked()
        playControlImageViewClicked()
        skipNextImageViewClicked()
    }

    private fun notificationBuilder(): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
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

    private fun notificationUI(artist: String, title: String, coverURL:String) {
        mContentView = RemoteViews(context.packageName, R.layout.notification)
        GlideApp.with(context)
            .asBitmap()
            .load(coverURL)
            .into(object : CustomTarget<Bitmap>() {
                //load 에 명시한 이미지를 불러왔을 때 자동으로 호출됨
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    mContentView.setImageViewBitmap(R.id.coverImage, resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        mContentView.setTextViewText(R.id.title, "$title")
        mContentView.setTextViewText(R.id.artist, "$artist")

        if(player!!.isPlaying) {
            mContentView.setImageViewResource(R.id.playControlImageView, R.drawable.ic_baseline_play_arrow_24)
        }
        else {
            mContentView.setImageViewResource(R.id.playControlImageView, R.drawable.ic_baseline_pause_48)
        }


    }

    private fun skipPrevImageViewClicked() {
        val skipNextIntent = Intent(context, ForegroundService::class.java)
        skipNextIntent.action = SKIP_PREV

        val pendingIntent = PendingIntent.getService(
            context,
            SKIP_PREV_REQ_CODE,
            skipNextIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        mContentView.setOnClickPendingIntent(R.id.skipPrevImageView, pendingIntent)
    }

    private fun playControlImageViewClicked() {
        val playControlIntent = Intent(context, ForegroundService::class.java)
        playControlIntent.action = PLAY_CONTROL

        val pendingIntent = PendingIntent.getService(
            context,
            PLAY_CONTROL_REQ_CODE,
            playControlIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        mContentView.setOnClickPendingIntent(R.id.playControlImageView, pendingIntent)
    }

    private fun skipNextImageViewClicked() {
        val skipNextIntent = Intent(context, ForegroundService::class.java)
        skipNextIntent.action = SKIP_NEXT

        val pendingIntent = PendingIntent.getService(
            context,
            SKIP_NEXT_REQ_CODE,
            skipNextIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        mContentView.setOnClickPendingIntent(R.id.skipNextImageView, pendingIntent)
    }
}