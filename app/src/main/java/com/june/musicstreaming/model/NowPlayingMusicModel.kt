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