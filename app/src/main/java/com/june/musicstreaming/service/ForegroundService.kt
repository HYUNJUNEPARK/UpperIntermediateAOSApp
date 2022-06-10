package com.june.musicstreaming.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Notification(this).notifyNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}