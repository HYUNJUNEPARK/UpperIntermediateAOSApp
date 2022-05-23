package com.june.youtube.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.june.youtube.fragment.PlayerFragment
import com.june.youtube.R
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.constant.Constants.Companion.BASE_URL
import com.june.youtube.constant.Constants.Companion.TAG
import com.june.youtube.databinding.ActivityMainBinding
import com.june.youtube.dto.VideoDto
import com.june.youtube.service.VideoService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    //TODO List Adapter
    private lateinit var videoAdapter: VideoAdapter

    //private lateinit var recyclerViewAdapter: VideoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//TODO List Adapter
        videoAdapter = VideoAdapter()

        binding.mainRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

//        recyclerViewAdapter = VideoRecyclerViewAdapter()
//        binding.mainRecyclerView.apply {
//            adapter = recyclerViewAdapter
//            layoutManager = LinearLayoutManager(context)
//        }


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
                            //recyclerViewAdapter.videoList = videoDto.videos

                        //TODO List Adapter
                        videoAdapter.submitList(videoDto.videos)
                        }
                    }
                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        //exception handling
                        Toast.makeText(this@MainActivity, "Fail to load videos", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}