package com.june.youtube.activity

import android.os.Bundle
import android.util.Log
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
import com.june.youtube.retrofit.MyRetrofit
import com.june.youtube.retrofit.VideoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var progressBar: ProgressBar
    }
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val networkCheck: NetworkConnectionCallback by lazy { NetworkConnectionCallback(this) }
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        networkCheck.register()
        progressBar = findViewById(R.id.progressBar)

        attachFragment()
        initRecyclerView()
        MyRetrofit(this, videoAdapter).videoList()
    }

    override fun onDestroy() {
        super.onDestroy()

        networkCheck.unregister()
    }

    private fun initRecyclerView() {
        videoAdapter = VideoAdapter(itemClickedListener = { uri, title ->
            supportFragmentManager.fragments.find { fragment ->
                fragment is PlayerFragment }?.let { fragment ->
                    (fragment as PlayerFragment).play(uri,title)
            }
        })
        binding.mainRecyclerView.adapter = videoAdapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()
    }
}