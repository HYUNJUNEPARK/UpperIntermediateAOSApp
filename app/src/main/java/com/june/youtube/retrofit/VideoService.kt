package com.june.youtube.retrofit

import com.june.youtube.retrofit.Constants.Companion.MOCK_API
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET(MOCK_API)
    fun listVideos(): Call<VideoDto>
}