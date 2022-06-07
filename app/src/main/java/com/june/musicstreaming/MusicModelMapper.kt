package com.june.musicstreaming

import com.june.musicstreaming.retrofit.MusicEntity

//MusicEntity 를 확장시켜 Music 모델로 사용
fun MusicEntity.mapper(id: Long): MusicRecyclerViewModel =
    MusicRecyclerViewModel(
        id = id,
        streamUrl = this.streamUrl,
        coverUrl = this.coverUrl,
        track = this.track,
        artist = this.artist
    )