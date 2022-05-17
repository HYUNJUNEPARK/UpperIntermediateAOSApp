package com.june.youtube.service

import com.june.youtube.constant.Constants.Companion.MOCK_API
import com.june.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET(MOCK_API)
    fun listVideos(): Call<VideoDto>
}