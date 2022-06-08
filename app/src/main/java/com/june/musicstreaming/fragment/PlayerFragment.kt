package com.june.musicstreaming.fragment

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.june.musicstreaming.adapter.PlayListAdapter
import com.june.musicstreaming.R
import com.june.musicstreaming.databinding.FragmentPlayerBinding
import com.june.musicstreaming.retrofit.MusicRetrofit

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(R.layout.fragment_player) {
    private var isWatchingPlayerListView = true
    private lateinit var playListAdapter: PlayListAdapter

    private var player: SimpleExoPlayer ? = null

    override fun initView() {
        super.initView()
        binding.playerFragment = this

        initPlayView()
        initRecyclerView()
        MusicRetrofit().retrofitCreate(playListAdapter) //get music list from server
    }

    private fun initPlayView() {

    }

    private fun initRecyclerView() {
        playListAdapter = PlayListAdapter {
            //TODO 음악 재생
        }

        binding.playListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
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
    }
}