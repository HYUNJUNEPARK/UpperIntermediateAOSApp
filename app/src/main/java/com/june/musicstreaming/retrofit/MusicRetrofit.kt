package com.june.musicstreaming.retrofit

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MusicRetrofit {
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MockUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun retrofitCreate() {
        val retrofit = retrofit()

        CoroutineScope(Dispatchers.IO).launch {
            retrofit.create(MusicInterface::class.java)
                .also { musicInterface ->
                    musicInterface.listMusic()
                        .enqueue(object : Callback<MusicDto> {
                            override fun onResponse(call: Call<MusicDto>, response: Response<MusicDto>) {
                                Log.d("testLog", "onResponse: ${response.body()}")
                            }
                            override fun onFailure(call: Call<MusicDto>, t: Throwable) { }
                        })
                }
        }
    }
}