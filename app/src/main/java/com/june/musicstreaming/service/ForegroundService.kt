package com.june.musicstreaming.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.june.musicstreaming.exoPlayer.ExoPlayer
import com.june.musicstreaming.exoPlayer.ExoPlayer.Companion.player
import com.june.musicstreaming.model.NowPlayingMusicModel
import com.june.musicstreaming.service.Constant.Companion.PLAY_CONTROL
import com.june.musicstreaming.service.Constant.Companion.SKIP_NEXT
import com.june.musicstreaming.service.Constant.Companion.SKIP_PREV

class ForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //TODO 음악 컨트롤 할 것 -> 알람에 UI 세팅 먼저
        //TODO 이 부분이 두번 실행되는 버그 있음 찾아서 수정할 것


        //TODO 중복 코드 playerFragment
        //notification UI
        val artist = NowPlayingMusicModel.nowPlayingMusic?.artist.toString()
        val title = NowPlayingMusicModel.nowPlayingMusic?.track.toString()
        val coverURL = NowPlayingMusicModel.nowPlayingMusic?.coverUrl.toString()
        Notification(this).notifyNotification(artist, title, coverURL)

        //Button Action
        if (intent?.action == PLAY_CONTROL) {
            Log.d("testLog", "play")
            if (player!!.isPlaying) {
                player!!.pause()
            }
            else {
                player!!.play()
            }
        }

        if (intent?.action == SKIP_NEXT) {
            Log.d("testLog", "skip next")
        }

        if (intent?.action == SKIP_PREV) {
            Log.d("testLog", "skip prev")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}