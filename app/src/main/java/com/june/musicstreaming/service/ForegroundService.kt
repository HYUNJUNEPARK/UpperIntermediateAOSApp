package com.june.musicstreaming.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.june.musicstreaming.model.NowPlayingMusicModel.Companion.nowPlayingMusic

class ForegroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val artist = nowPlayingMusic?.artist.toString()
        val title = nowPlayingMusic?.track.toString()

        Notification(this).notifyNotification(artist, title)

        //TODO 음악 컨트롤 할 것 -> 알람에 UI 세팅 먼저
        //TODO 이 부분이 두번 실행되는 버그 있음 찾아서 수정할 것
        Log.d("testLog", "onStartCommand: aaaaaaaaaaa")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}