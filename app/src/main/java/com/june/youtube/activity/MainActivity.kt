package com.june.youtube.activity

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.june.youtube.R
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.retrofit.Constants.Companion.BASE_URL
import com.june.youtube.databinding.ActivityMainBinding
import com.june.youtube.retrofit.VideoDto
import com.june.youtube.fragment.PlayerFragment
import com.june.youtube.network.NetworkConnectionCallback
import com.june.youtube.retrofit.VideoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private lateinit var videoAdapter: VideoAdapter
    private val networkCheck: NetworkConnectionCallback by lazy { NetworkConnectionCallback(this) }

    companion object {
        lateinit var progressBar: ProgressBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        networkCheck.register()

        progressBar = findViewById(R.id.progressBar)
        attachFragment()
        videoList()
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()

        networkCheck.unregister()
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
        CoroutineScope(Dispatchers.IO).launch {
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
}