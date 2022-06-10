package com.june.musicstreaming.exoPlayer

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.june.musicstreaming.model.MusicModel
import com.june.musicstreaming.model.NowPlayingMusicModel
import com.june.musicstreaming.service.Constant.Companion.PLAYER_INTENT_ACTION
import com.june.musicstreaming.service.ForegroundService
import com.june.musicstreaming.service.Notification

class ExoPlayer {
    companion object {
        var player: SimpleExoPlayer? = null
    }

    fun initExoPlayer(context: Context) {
        player = SimpleExoPlayer.Builder(context).build()
    }

    fun play(item: MusicModel, context: Context) {
        NowPlayingMusicModel.nowPlayingMusic = item

        val url = item.streamUrl
        val dataSourceFactory = DefaultDataSourceFactory(context)
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        val progressiveMediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        player?.setMediaSource(progressiveMediaSource)
        player?.prepare() //데이터 가져옴
        player?.play()

        val intent = Intent(context, ForegroundService::class.java)
        intent.action = PLAYER_INTENT_ACTION
        context.startService(intent)

        //Notification(context).notifyNotification()
    }
}