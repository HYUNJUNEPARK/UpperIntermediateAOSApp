package com.june.myapplication.data

import android.util.Log
import com.june.myapplication.BuildConfig
import com.june.myapplication.MainActivity.Companion.TAG
import com.june.myapplication.data.services.AirKoreaApiService
import com.june.myapplication.data.services.KakaoLocalApiService
import fastcampus.aop.part4.chapter06.data.models.monitoringstation.MonitoringStation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {
    suspend fun getNearbyMonitoringStation(latitude: Double, longitude: Double):MonitoringStation? {
        Log.d(TAG, "getNearbyMonitoringStation: $latitude\n$longitude")

        val tmCoordinates = kakaoLocalApiService
            .getTmCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()
        //TODO 여기서 문제가 있음
        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y
        //Log.d(TAG, "tmx : $tmX // $tmY")

        return airKoreaApiService
            .getNearbyMonitoringStation(tmX!!, tmY!!)
            .body() //retrofit 응답 body
            ?.response
            ?.body //응답 body 내부에 있는 body
            ?.monitoringStations
            ?.minByOrNull { it.tm ?: Double.MAX_VALUE } //선택한 요소 중 가장 작은 값은 반환
    }

    private val kakaoLocalApiService: KakaoLocalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient()) //로깅을 클라이언트에서 추가
            .build()
            .create()
    }

    private val airKoreaApiService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.AIR_KOREA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
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