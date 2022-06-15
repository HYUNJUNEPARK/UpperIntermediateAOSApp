package com.june.musicstreaming.retrofit

import com.june.musicstreaming.retrofit.MockUrl.Companion.DETAIL_URL
import retrofit2.Call
import retrofit2.http.GET

interface MusicInterface {
    @GET(DETAIL_URL)
    fun listMusic() : Call<MusicDto>
}