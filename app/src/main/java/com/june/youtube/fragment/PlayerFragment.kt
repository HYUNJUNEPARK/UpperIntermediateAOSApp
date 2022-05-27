package com.june.youtube.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.june.youtube.R
import com.june.youtube.activity.MainActivity
import com.june.youtube.adapter.VideoAdapter
import com.june.youtube.databinding.FragmentPlayerBinding
import com.june.youtube.retrofit.Constants.Companion.TAG
import com.june.youtube.retrofit.MyRetrofit
import kotlin.math.abs

class PlayerFragment: Fragment(R.layout.fragment_player) {
    private var player: SimpleExoPlayer? = null
    private lateinit var videoAdapter: VideoAdapter
    private var _binding : FragmentPlayerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        motionSyncFragmentMotionLayoutAndMainMotionLayout()
        initRecyclerView()
        MyRetrofit(requireContext(), videoAdapter).videoList()
        initPlayer()
        initControlButton()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
        player?.release()
    }

    override fun onStop() {
        super.onStop()

        player?.pause()
    }

    private fun initRecyclerView() {
        videoAdapter = VideoAdapter(itemClickedListener = { url, title ->
            play(url, title)
        })
        binding.fragmentRecyclerView.adapter = videoAdapter
        binding.fragmentRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun motionSyncFragmentMotionLayoutAndMainMotionLayout() {
        binding.playerMotionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) { }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) { }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) { }
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                binding.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
                    }
                }
            }
        })
    }

    fun play(url: String, title: String) {
        //dataSource -> mediaSource
        context?.let { context ->
            val dataSourceFactory = DefaultDataSourceFactory(context)
            val progressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            player?.setMediaSource(progressiveMediaSource)
            player?.prepare() //데이터 가져옴
            player?.play()
        }
        binding.playerMotionLayout.transitionToEnd()
        binding.bottomTitleTextView.text = title
    }

    private fun initPlayer() {
        context?.let { context ->
            player = SimpleExoPlayer.Builder(context).build()
        }
        binding.playerView.player = player

        player?.addListener(object: Player.EventListener {
            //play 여부가 바뀔 때 마다 호출 됨
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    binding.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    Log.d(TAG, "onIsPlayingChanged: playing")
                }
                else {
                    binding.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_pause_24)
                    Log.d(TAG, "onIsPlayingChanged: pause")
                }
            }
        })
    }

    private fun initControlButton() {
        binding.bottomPlayerControlButton.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            }
            else {
                player.play()
            }
        }
    }

}