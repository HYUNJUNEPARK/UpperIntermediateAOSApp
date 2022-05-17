package com.june.youtube.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.june.youtube.fragment.PlayerFragment
import com.june.youtube.R
import com.june.youtube.constant.Constants.Companion.BASE_URL
import com.june.youtube.constant.Constants.Companion.TAG
import com.june.youtube.databinding.ActivityMainBinding
import com.june.youtube.dto.VideoDto
import com.june.youtube.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        attachFragment()
        getVideoList()
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(VideoService::class.java).also { videoService ->
            videoService.listVideos()
                .enqueue(object: Callback<VideoDto>{
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.e(TAG, "MainActivity onResponse: FAIL ")
                            return
                        }
                        response.body()?.let { videoDto ->
                            Log.d(TAG, "MainActivity  onResponse response body: $videoDto")
                        }
                    }
                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        //exception handling
                    }
                })
        }
    }
}