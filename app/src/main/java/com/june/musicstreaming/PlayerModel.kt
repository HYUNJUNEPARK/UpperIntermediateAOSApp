package com.june.musicstreaming

data class PlayerModel(
    val playMusicList: List<MusicListModel>,
    var currentPosition: Int = -1,

)