package com.june.musicstreaming.retrofit

import android.view.View
import com.june.musicstreaming.adapter.PlayListAdapter
import com.june.musicstreaming.fragment.PlayerFragment.Companion.allMusicList
import com.june.musicstreaming.fragment.PlayerFragment.Companion.progressBar
import com.june.musicstreaming.model.NowPlayingMusicModel
import com.june.musicstreaming.model.mapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicRetrofit {
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MockUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun retrofitCreate(playListAdapter: PlayListAdapter) {
        CoroutineScope(Dispatchers.IO).launch {
            retrofit().run {
                create(MusicInterface::class.java)
                    .also { musicInterface ->
                        musicInterface.listMusic().enqueue( object : Callback<MusicDto> {
                            override fun onResponse(call: Call<MusicDto>, response: Response<MusicDto>) {
                                response.body()?.let { musicDto ->
                                    allMusicList = musicDto.musics.mapIndexed { index, musicEntity ->
                                        musicEntity.mapper(index.toLong())
                                    }
                                    NowPlayingMusicModel.musicListSize = allMusicList?.size
                                    playListAdapter.submitList(allMusicList)
                                    progressBar.visibility = View.INVISIBLE
                                }
                            }
                            override fun onFailure(call: Call<MusicDto>, t: Throwable) { }
                        })
                    }
            }
        }
    }
}