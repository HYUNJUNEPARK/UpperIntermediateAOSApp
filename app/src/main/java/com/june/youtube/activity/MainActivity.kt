package com.june.youtube.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.june.youtube.R
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.constant.Constants.Companion.BASE_URL
import com.june.youtube.databinding.ActivityMainBinding
import com.june.youtube.dto.VideoDto
import com.june.youtube.fragment.PlayerFragment
import com.june.youtube.service.VideoService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        attachFragment()
        videoList()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        videoAdapter = VideoAdapter()
        binding.mainRecyclerView.adapter = videoAdapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()
    }

    private fun videoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also { videoService ->
            videoService.listVideos()
                .enqueue(object: Callback<VideoDto>{
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Toast.makeText(this@MainActivity, "MainActivity onResponse: FAIL", Toast.LENGTH_SHORT).show()
                            return
                        }
                        response.body()?.let { videoDto ->
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