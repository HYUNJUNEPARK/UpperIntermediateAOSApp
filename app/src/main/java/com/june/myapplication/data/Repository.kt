package com.june.myapplication.data

import com.june.myapplication.BuildConfig
import com.june.myapplication.data.services.KakaoLocalApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {
    suspend fun getNearbyMonitoringStation(latitude: Double, longitude: Double) {
        val tmCoordinates = kakaoLocalApiService
            .getTmCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()

        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y
    }

    private val kakaoLocalApiService: KakaoLocalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient()) //로깅을 클라이언트에서 추가
            .build()
            .create()
    }

    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                /**
                 * 레벨(level)에 따라 보이는 정보가 달라짐
                 * 개발 시(디버깅 시)는 로그가 다 보이는 게 좋으나 배포 시에는 로그가 다 보이면 보안 이슈가 발생할 수 있음
                 * HttpLoggingInterceptor.Level.BODY :
                 * HttpLoggingInterceptor.Level.NONE :
                 */
                HttpLoggingInterceptor().apply {

                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    }
                    else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
}