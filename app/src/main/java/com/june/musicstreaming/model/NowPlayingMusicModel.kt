package com.june.musicstreaming.model

import android.util.Log
import com.june.musicstreaming.fragment.PlayerFragment.Companion.allMusicList

class NowPlayingMusicModel {
    companion object {
        var musicListSize: Int? = null
        var nowPlayingMusic: MusicModel? = null
    }

    fun nextMusic() {
        val nextMusicIdx: Int = nowPlayingMusic!!.id.toInt() + 1
        if (nextMusicIdx == musicListSize!!) return

        nowPlayingMusic = allMusicList!![nextMusicIdx]
    }

    fun prevMusic() {
        val prevMusicIdx: Int = nowPlayingMusic!!.id.toInt() - 1
        if (prevMusicIdx < 0) return

        nowPlayingMusic = allMusicList!![prevMusicIdx]
    }
}