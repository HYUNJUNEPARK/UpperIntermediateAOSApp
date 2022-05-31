package com.june.youtube.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.june.youtube.R
import com.june.youtube.activity.MainActivity
import com.june.youtube.databinding.FragmentPlayerBinding

abstract class BaseExoPlayerFragment<T: FragmentPlayerBinding>(@LayoutRes private val layoutId: Int): Fragment() {
    var player: SimpleExoPlayer? = null
    private var _binding: T? = null
    protected val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
        player?.release()
    }

    override fun onStart() {
        super.onStart()

        player?.pause()
    }

    protected open fun initView() {
        initPlayer()
        initControlButton()
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
        binding.playerMotionLayout.transitionToStart()
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
                    binding.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_pause_24)
                }
                else {
                    binding.bottomPlayerControlButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
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
        binding.closeButton.setOnClickListener {
            MainActivity.fragmentContainer.visibility = View.INVISIBLE
        }
    }
}