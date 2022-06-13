package com.june.musicstreaming.service

class Constant {
    companion object {
        const val CHANNEL_ID = "Channel for music play"
        const val CHANNEL_NAME = "Music Player"
        const val PLAYER_INTENT_ACTION = "myMusicPlayer"
        const val ACTION_PENDING_INTENT_REQ_CODE = 99
        const val NOTIFICATION_TITLE = "Music Player"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_BUTTON_TITLE = "TEST"

        const val SKIP_NEXT = "skip next"
        const val SKIP_NEXT_REQ_CODE = 1

        const val SKIP_PREV = "skip prev"
        const val SKIP_PREV_REQ_CODE = 2

        const val PLAY_CONTROL = "play by notification"
        const val PLAY_CONTROL_REQ_CODE = 3
    }
}