package com.june.youtube.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.june.youtube.R
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.databinding.ActivityMainBinding
import com.june.youtube.fragment.PlayerFragment
import com.june.youtube.network.NetworkConnectionCallback
import com.june.youtube.retrofit.VideoRetrofit

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var progressBar: ProgressBar
        lateinit var fragmentContainer: FrameLayout
    }
    private lateinit var binding: ActivityMainBinding
    private val networkCheck: NetworkConnectionCallback by lazy { NetworkConnectionCallback(this) }
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        networkCheck.register()
        progressBar = binding.progressBar
        fragmentContainer = binding.fragmentContainer

        attachFragment()
        initRecyclerView()
        VideoRetrofit(this, videoAdapter).videoList()
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