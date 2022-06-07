package com.june.youtube.retrofit

import com.june.youtube.retrofit.MockUrl.Companion.MOCK_API
import retrofit2.Call
import retrofit2.http.GET

interface VideoRetrofitInterface {
    @GET(MOCK_API)
    fun listVideos(): Call<VideoDto>
}