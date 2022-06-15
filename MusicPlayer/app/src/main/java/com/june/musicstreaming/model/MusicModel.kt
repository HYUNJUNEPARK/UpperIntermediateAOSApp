package com.june.musicstreaming.model

class MusicModel (
    val id: Long,
    val track: String,
    val streamUrl: String,
    val artist: String,
    val coverUrl: String,
    var isPlaying: Boolean = false
)