package com.june.youtube.retrofit

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.june.youtube.adapter.VideoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoRetrofit(val context: Context, val videoAdapter: VideoAdapter) {
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MockUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun videoList() {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = retrofit()

            retrofit.create(VideoRetrofitInterface::class.java).also { videoService ->
                videoService.listVideos().enqueue(object: Callback<VideoDto> {
                        override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                            if (response.isSuccessful.not()) {
                                Toast.makeText(context, "MainActivity onResponse: FAIL", Toast.LENGTH_SHORT).show()
                                return
                            }
                            response.body()?.let { videoDto ->
                                Log.d("testLog", "MainActivity fun videoList videoDto: $videoDto")
                                videoAdapter.submitList(videoDto.videos)
                            }
                        }
                        override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                            //exception handling
                            Toast.makeText(context, "Fail to load videos", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}