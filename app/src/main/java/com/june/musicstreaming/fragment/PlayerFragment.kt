package com.june.musicstreaming.fragment

import android.content.Context
import android.util.Log
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.june.musicstreaming.R
import com.june.musicstreaming.adapter.PlayListAdapter
import com.june.musicstreaming.databinding.FragmentPlayerBinding
import com.june.musicstreaming.retrofit.MusicRetrofit
import com.june.musicstreaming.retrofit.MusicRetrofit.Companion.musicList

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(R.layout.fragment_player) {
    private var isWatchingPlayerListView = true
    private lateinit var playListAdapter: PlayListAdapter

    //TODO EXO player
    private var player: SimpleExoPlayer ? = null

    override fun initView() {
        super.initView()
        binding.playerFragment = this
        progressBar = binding.progressBar

        initPlayView()
        initPlayControlButtons()
        initRecyclerView()
        MusicRetrofit().retrofitCreate(playListAdapter) //get music list from server


//        binding.skipNextImageView.setOnClickListener {
//            setMusicList()
//        }
    }

    private fun setMusicList() {
        Log.d(TAG, "${musicList}")
        player?.addMediaItems(musicList.map { musicListModel ->
            Log.d(TAG, "setMusicList: ${musicListModel.streamUrl}")
            MediaItem.Builder()
                .setMediaId(musicListModel.id.toString())
                .setUri(musicListModel.streamUrl)
                .build()
        })
        player?.prepare()
        player?.play()
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
        })
    }

    //TODO EXO player
//    fun setMusicList(modelList: List<MusicListModel>) {
//        Log.d(TAG, "setMusicList: ${modelList[0].streamUrl}")
//
//        player?.addMediaItems(modelList.map { musicListModel ->
//            Log.d(TAG, "setMusicList: ${musicListModel.streamUrl}")
//            MediaItem.Builder()
//                .setMediaId(musicListModel.id.toString())
//                .setUri("https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/000/933/badniss-1618491636-ZXAKvq3LYz.mp3")
//                .build()
//        })
//        player?.prepare()
//        player?.play()
//    }




    private fun initRecyclerView() {
        playListAdapter = PlayListAdapter {
            //TODO 음악 재생
        }

        binding.playListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    private fun initPlayControlButtons() {
        binding.playControlImageView.setOnClickListener {
            Log.d(TAG, "initPlayControlButtons: Clicked ")
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            }
            else {
                player.play()
            }
        }
        binding.skipNextImageView.setOnClickListener {
        }
        binding.skipPrevImageView.setOnClickListener {
        }
    }


    fun onPlayListButtonClicked() {
        //todo :  만약 서버에서 데이터가 다 불려오지 않은 상태일 때 예외처리 코드 필요
        binding.playerViewGroup.isVisible = isWatchingPlayerListView
        binding.playListViewGroup.isVisible = isWatchingPlayerListView.not()
        isWatchingPlayerListView = isWatchingPlayerListView.not()
    }



    //액티비티에서 video Id 값을 뉴인스턴스에 넘겨주거나 할 때 apply 함수를 통해서 쉽게 arguments 에 추가할 수 있음
    //PlayerFragment 를 직접 만드는 것보다 프래그먼트를 감싸는 인스턴스를 만들어서 넣어주는 게 어떤 점에서 좋지 ?
    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
        lateinit var progressBar: ProgressBar
        const val TAG = "testLog"
    }
}