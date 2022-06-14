package com.june.musicstreaming.exoPlayer

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.june.musicstreaming.model.MusicModel
import com.june.musicstreaming.service.ForegroundService

class ExoPlayer {
    companion object {
        //initialized in PlayFragment initView
        var player: SimpleExoPlayer? = null
    }

    //initialized in PlayFragment initView
    fun initExoPlayer(context: Context) {
        player = SimpleExoPlayer.Builder(context).build()
    }

    fun play(item: MusicModel, context: Context) {
        val url = item.streamUrl
        val dataSourceFactory = DefaultDataSourceFactory(context)
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        val progressiveMediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        player?.setMediaSource(progressiveMediaSource)
        player?.prepare() //데이터 가져옴
        player?.play()

        //음악을 재생할 때마다 서비스를 다시 실행 시킴(Notification 다시 보냄)
        val intent = Intent(context, ForegroundService::class.java)
        context.startService(intent)
    }
}