package com.june.youtube.activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.june.youtube.R
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.databinding.ActivityMainBinding
import com.june.youtube.fragment.PlayerFragment
import com.june.youtube.network.NetworkConnectionCallback
import com.june.youtube.retrofit.MyRetrofit

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var progressBar: ProgressBar
        lateinit var fragmentContainer: FrameLayout
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val networkCheck: NetworkConnectionCallback by lazy { NetworkConnectionCallback(this) }
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        networkCheck.register()
        progressBar = binding.progressBar
        fragmentContainer = binding.fragmentContainer

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