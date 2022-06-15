package com.june.musicstreaming.model

import android.app.AlertDialog
import android.content.Context

class NowPlayingMusicDialog(private val context: Context) {
    val nonNextMusicDialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("다음 재생 곡이 없습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
    }

    val nonPrevMusicDialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("이전 재생 곡이 없습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
    }
}