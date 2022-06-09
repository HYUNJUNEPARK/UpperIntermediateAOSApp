package com.june.musicstreaming.fragment

import android.content.Context
import android.net.Uri
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.june.musicstreaming.model.MusicListModel
import com.june.musicstreaming.R
import com.june.musicstreaming.adapter.PlayListAdapter
import com.june.musicstreaming.databinding.FragmentPlayerBinding
import com.june.musicstreaming.retrofit.MusicRetrofit

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(R.layout.fragment_player) {
    //액티비티에서 video Id 값을 뉴인스턴스에 넘겨주거나 할 때 apply 함수를 통해서 쉽게 arguments 에 추가할 수 있음
    //PlayerFragment 를 직접 만드는 것보다 프래그먼트를 감싸는 인스턴스를 만들어서 넣어주는 게 어떤 점에서 좋지 ?
    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
        //플레이 중인 음악 객체를 채울 변수
        var playingModel: MusicListModel? = null
        lateinit var progressBar: ProgressBar
        const val TAG = "testLog"
        var musicList: List<MusicListModel>? = null
        //TODO EXO player
        var player: SimpleExoPlayer ? = null
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
    }

    fun play(item: MusicListModel, context: Context) {
        val url = item.streamUrl
        val dataSourceFactory = DefaultDataSourceFactory(context)
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        val progressiveMediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        player?.setMediaSource(progressiveMediaSource)
        player?.prepare() //데이터 가져옴
        player?.play()

    }

    fun updatePlayerView(item: MusicListModel?) {
        binding.trackTextView.text = item?.track
        binding.artistTextView.text = item?.artist

        Glide.with(binding.coverImageView.context)
            .load(item?.coverUrl)
            .into(binding.coverImageView)

        Glide.with(binding.coverThumbNail.context)
            .load(item?.coverUrl)
            .into(binding.coverThumbNail)
    }

    private fun initPlayView() {
        context?.let { context ->
            player = SimpleExoPlayer.Builder(context).build()
        }
        binding.playerView.player = player
        player?.addListener(object: Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                }
                else {
                    binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
            }

            //TODO 여기에다가 음악이 바뀔 때마다 PlayerView binding 해서 바꿔주기
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                updatePlayerView(playingModel)
            }
        })
    }

    private fun initRecyclerView() {
        playListAdapter = PlayListAdapter(requireContext())
        binding.playListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun playControlButtonClicked() {
        val player = player ?: return
        if (player.isPlaying) {
            player.pause()
        }
        else {
            player.play()
        }
    }

    fun playNextMusicButtonClicked() {

    }

    fun playPrevMusicButtonClicked() {

    }

    fun onPlayListButtonClicked() {
        //todo :  만약 서버에서 데이터가 다 불려오지 않은 상태일 때 예외처리 코드 필요
        binding.playerViewGroup.isVisible = isWatchingPlayerListView
        binding.playListViewGroup.isVisible = isWatchingPlayerListView.not()
        isWatchingPlayerListView = isWatchingPlayerListView.not()
    }
}