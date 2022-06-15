package com.june.youtube.network

import android.app.Activity
import android.app.AlertDialog
import android.content.Context

class NetworkDialog (private val context: Context) {
    val unConnectionDialog: AlertDialog by lazy {
        AlertDialog.Builder(context)
            .setTitle("Network Error")
            .setMessage("앱을 종료합니다.")
            .setPositiveButton("확인") { _, _ ->
                val activity = context as Activity
                activity.finish()
            }
            .create()
    }
}