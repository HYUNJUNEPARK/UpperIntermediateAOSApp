package com.june.musicstreaming.ExoPlayer

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.june.musicstreaming.model.MusicListModel

class ExoPlayer {
    companion object {
        var player: SimpleExoPlayer? = null
    }

    fun initExoPlayer(context: Context) {
        player = SimpleExoPlayer.Builder(context).build()
    }

    fun play(item: MusicListModel, context: Context) {
        val url = item.streamUrl
        val dataSourceFactory = DefaultDataSourceFactory(context)
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        val progressiveMediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        player?.setMediaSource(progressiveMediaSource)
        player?.prepare() //데이터 가져옴
        player?.play()
    }
}