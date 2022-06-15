package com.june.musicstreaming.model

import android.content.Context
import com.june.musicstreaming.fragment.PlayerFragment.Companion.allMusicList

class NowPlayingMusicModel {
    companion object {
        var musicListSize: Int? = null
        var nowPlayingMusic: MusicModel? = null
    }

    fun nextMusic(context: Context) {
        val nextMusicIdx: Int = nowPlayingMusic!!.id.toInt() + 1
        if (nextMusicIdx == musicListSize!!) {
            NowPlayingMusicDialog(context).nonNextMusicDialog.show()
            return
        }
        else {
            nowPlayingMusic = allMusicList!![nextMusicIdx]
        }
    }

    fun prevMusic(context: Context) {
        val prevMusicIdx: Int = nowPlayingMusic!!.id.toInt() - 1
        if (prevMusicIdx < 0) {
            NowPlayingMusicDialog(context).nonPrevMusicDialog.show()
            return
        }
        else {
            nowPlayingMusic = allMusicList!![prevMusicIdx]
        }
    }
//앱이 백그라운드 상태에 있을 때 AlertDialog 를 띄우면 앱이 크래시되기 때문에 아래 함수를 사용
    fun notificationNextMusic() {
        val nextMusicIdx: Int = nowPlayingMusic!!.id.toInt() + 1
        if (nextMusicIdx == musicListSize!!) {
            return
        }
        else {
            nowPlayingMusic = allMusicList!![nextMusicIdx]
        }
    }

    fun notificationPrevMusic() {
        val prevMusicIdx: Int = nowPlayingMusic!!.id.toInt() - 1
        if (prevMusicIdx < 0) {
            return
        }
        else {
            nowPlayingMusic = allMusicList!![prevMusicIdx]
        }
    }
}