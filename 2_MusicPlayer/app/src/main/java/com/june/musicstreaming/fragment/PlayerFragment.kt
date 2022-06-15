package com.june.musicstreaming.fragment

import android.graphics.Color
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.june.musicstreaming.R
import com.june.musicstreaming.adapter.PlayListAdapter
import com.june.musicstreaming.databinding.FragmentPlayerBinding
import com.june.musicstreaming.exoPlayer.ExoPlayer
import com.june.musicstreaming.exoPlayer.ExoPlayer.Companion.player
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
        lateinit var progressBar: ProgressBar //AOS context class 를 메모리 누수 때문에 companion object 애 사용하지 않는 것을 권장
        var allMusicList: List<MusicModel>? = null //initialized in MusicRetrofit
    }
    private val updateSeekRunnable = Runnable {
        //1000ms 딜레이 후 실행 될 코드
        updateSeekBar()
    }
    private var isWatchingPlayerListView = true
    private lateinit var playListAdapter: PlayListAdapter

    override fun initView() {
        super.initView()

        binding.playerFragment = this //for dataBinding
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
            //Playing 상태가 바뀔 때마다 하단 컨트롤러 버튼 ui 를 바꿔 줌
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                Notification(requireContext()).notifyNotification()

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
            //새로운 음원이 재생될 때마다 Play 커버와 하단 컨트롤러 앨범 커버 업데이트
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
            player.pause()//음악정지
            NowPlayingMusicModel.nowPlayingMusic?.isPlaying = false
            Notification(requireContext()).notifyNotification()
        }
        else {
            player.play() //음악 재생
            NowPlayingMusicModel.nowPlayingMusic?.isPlaying = true
            Notification(requireContext()).notifyNotification()
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
        //하단부 작게 있는 플레이리스트 SeekBar 의 경우 터치로 재생 구간을 이동시키지 않게 만듬
        binding.playListSeekBar.setOnTouchListener { _, _ ->
            false
        }
        //재생 중인 음원 디테일이 보이는 플레이어 SeekBar 의 경우 사용자가 SeekBar 를 조작한다면 그 구간으로 재생 구간을 이동시킴
        binding.playerSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { }
            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                player?.seekTo((seekBar.progress*1000).toLong())
            }
        })
    }

    private fun updateSeekBar() {
        val player = player ?: return
        val duration = if (player.duration >= 0) player.duration else 0
        val position = player.currentPosition
        updateSeekBarUI(duration, position)

        //STATE_IDLE : 재생실패
        //STATE_ENDED : 재생마침
        val state = player.playbackState
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            //postDelayed : 앞의 과정이 약간의 시간이 필요하거나 바로 어떤 명령을 실행하지 않고 잠시 딜레이를 갖고 실행이 필요할 때 사용
            view?.postDelayed(updateSeekRunnable, 1000)
        }
    }

    private fun updateSeekBarUI(duration:Long, position: Long) {
        binding.let { binding ->
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