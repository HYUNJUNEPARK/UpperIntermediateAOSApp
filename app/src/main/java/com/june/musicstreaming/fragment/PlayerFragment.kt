package com.june.musicstreaming.fragment

import android.graphics.Color
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.june.musicstreaming.exoPlayer.ExoPlayer
import com.june.musicstreaming.exoPlayer.ExoPlayer.Companion.player
import com.june.musicstreaming.R
import com.june.musicstreaming.adapter.PlayListAdapter
import com.june.musicstreaming.databinding.FragmentPlayerBinding
import com.june.musicstreaming.model.MusicModel
import com.june.musicstreaming.model.NowPlayingMusicModel
import com.june.musicstreaming.retrofit.MusicRetrofit
import com.june.musicstreaming.service.Notification
import java.util.concurrent.TimeUnit

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(R.layout.fragment_player) {
    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
        lateinit var progressBar: ProgressBar //TODO AOS context class 를 메모리 누수 때문에 companion object 애 사용하지 않는 것을 권장
        var allMusicList: List<MusicModel>? = null //initialized in MusicRetrofit
    }
    //TODO Check what is Runnable
    private val updateSeekRunnable = Runnable {
        updateSeekBar()
    }
    private var isWatchingPlayerListView = true
    private lateinit var playListAdapter: PlayListAdapter

    override fun initView() {
        super.initView()
        binding.playerFragment = this
        progressBar = binding.progressBar

        initPlayView()
        initRecyclerView()
        MusicRetrofit().retrofitCreate(playListAdapter) //get music list from server
        initSeekBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        view?.removeCallbacks(updateSeekRunnable)
    }

    private fun initRecyclerView() {
        playListAdapter = PlayListAdapter(requireContext())
        binding.playListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initPlayView() {
        context?.let { context ->
            ExoPlayer().initExoPlayer(context)
        }
        binding.playerView.player = player
        player?.addListener(object: Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                    binding.playControlImageView.setColorFilter(Color.argb(255, 153, 0, 51))
                }
                else {
                    binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    binding.playControlImageView.setColorFilter(Color.argb(255, 0, 0, 0))
                }
            }
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                updateSeekBar()
            }
            //TODO 여기에다가 음악이 바뀔 때마다 PlayerView binding 해서 바꿔주기
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                updatePlayCoverView(NowPlayingMusicModel.nowPlayingMusic)
            }
        })
    }

    fun updatePlayCoverView(item: MusicModel?) {
        binding.trackTextView.text = item?.track
        binding.artistTextView.text = item?.artist

        Glide.with(binding.coverImageView.context)
            .load(item?.coverUrl)
            .into(binding.coverImageView)

        Glide.with(binding.coverThumbNail.context)
            .load(item?.coverUrl)
            .into(binding.coverThumbNail)
    }

//Play buttons
    fun playControlButtonClicked() {
        val player = player ?: return
        if (player.isPlaying) {
            player.pause()

            //TODO 중복코드 ForegroundService notification UI
            val artist = NowPlayingMusicModel.nowPlayingMusic?.artist.toString()
            val title = NowPlayingMusicModel.nowPlayingMusic?.track.toString()
            val coverURL = NowPlayingMusicModel.nowPlayingMusic?.coverUrl.toString()
            Notification(requireContext()).notifyNotification(artist, title, coverURL)
        }
        else {
            player.play()

            //notification UI
            val artist = NowPlayingMusicModel.nowPlayingMusic?.artist.toString()
            val title = NowPlayingMusicModel.nowPlayingMusic?.track.toString()
            val coverURL = NowPlayingMusicModel.nowPlayingMusic?.coverUrl.toString()
            Notification(requireContext()).notifyNotification(artist, title, coverURL)

        }
    }

    fun playNextMusicButtonClicked() {
        NowPlayingMusicModel().nextMusic(requireContext()).let {
            updatePlayCoverView(NowPlayingMusicModel.nowPlayingMusic)
            ExoPlayer().play(NowPlayingMusicModel.nowPlayingMusic!!, requireContext())
        }
    }

    fun playPrevMusicButtonClicked() {
        NowPlayingMusicModel().prevMusic(requireContext()).let {
            updatePlayCoverView(NowPlayingMusicModel.nowPlayingMusic)
            ExoPlayer().play(NowPlayingMusicModel.nowPlayingMusic!!, requireContext())
        }
    }

    fun playListButtonClicked() {
        binding.playerViewGroup.isVisible = isWatchingPlayerListView
        binding.playListViewGroup.isVisible = isWatchingPlayerListView.not()
        isWatchingPlayerListView = isWatchingPlayerListView.not()
    }

//SeekBar
    private fun initSeekBar() {
        binding.playerSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                player?.seekTo((seekBar.progress*1000).toLong())
            }
        })
        binding.playListSeekBar.setOnTouchListener { view, motionEvent ->
            false
        }
    }

    private fun updateSeekBar() {
        val player = player ?: return
        val duration = if (player.duration >= 0) player.duration else 0
        val position = player.currentPosition
        updateSeekBarUI(duration, position)

        val state = player.playbackState
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            view?.postDelayed(updateSeekRunnable, 1000)
        }
    }

    private fun updateSeekBarUI(duration:Long, position: Long) {
        binding?.let { binding ->
            //playList
            binding.playListSeekBar.max = (duration / 1000).toInt()
            binding.playListSeekBar.progress = (position / 1000).toInt()

            //player
            binding.playerSeekBar.max = (duration / 1000).toInt()
            binding.playerSeekBar.progress = (position / 1000).toInt()
            binding.playTimeTextView.text = String.format(
                    getString(R.string.play_time_format),
                    TimeUnit.MINUTES.convert(position, TimeUnit.MILLISECONDS),
                    (position / 1000) % 60
                )
            binding.totalTimeTextView.text = String.format(
                    getString(R.string.play_time_format),
                    TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS),
                    (duration / 1000) % 60
                )
        }
    }
}