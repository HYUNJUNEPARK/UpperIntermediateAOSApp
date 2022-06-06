package com.june.musicstreaming.fragment

import com.june.musicstreaming.R
import com.june.musicstreaming.databinding.FragmentPlayerBinding
import com.june.musicstreaming.retrofit.MusicRetrofit

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(R.layout.fragment_player) {
    override fun initView() {
        super.initView()

        getMusicListFromServer()
    }

    private fun getMusicListFromServer() {
        val musicRetrofit = MusicRetrofit()
        musicRetrofit.retrofitCreate()
    }

    //액티비티에서 video Id 값을 뉴인스턴스에 넘겨주거나 할 때 apply 함수를 통해서 쉽게 arguments 에 추가할 수 있음
    //PlayerFragment 를 직접 만드는 것보다 프래그먼트를 감싸는 인스턴스를 만들어서 넣어주는 게 어떤 점에서 좋지 ?
    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}