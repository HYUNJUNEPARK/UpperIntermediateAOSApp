package com.june.musicstreaming.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.june.musicstreaming.exoPlayer.ExoPlayer
import com.june.musicstreaming.exoPlayer.ExoPlayer.Companion.player
import com.june.musicstreaming.model.NowPlayingMusicModel
import com.june.musicstreaming.service.Constant.Companion.PLAY_CONTROL
import com.june.musicstreaming.service.Constant.Companion.SKIP_NEXT
import com.june.musicstreaming.service.Constant.Companion.SKIP_PREV

class ForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Notification(this).notifyNotification()

        //Button Action
        if (intent?.action == PLAY_CONTROL) {
            if (player!!.isPlaying) {
                player!!.pause()
            }
            else {
                player!!.play()
            }
        }

        if (intent?.action == SKIP_NEXT) {
            NowPlayingMusicModel().notificationNextMusic()
            ExoPlayer().play(NowPlayingMusicModel.nowPlayingMusic!!, this)
        }

        if (intent?.action == SKIP_PREV) {
            NowPlayingMusicModel().notificationPrevMusic()
            ExoPlayer().play(NowPlayingMusicModel.nowPlayingMusic!!, this)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}