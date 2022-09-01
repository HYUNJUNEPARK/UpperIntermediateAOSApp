package com.june.myapplication.retrofit

import com.june.myapplication.BuildConfig
import com.june.myapplication.Url
import com.june.myapplication.services.AirKoreaApiService
import com.june.myapplication.services.KakaoLocalApiService
import fastcampus.aop.part4.chapter06.data.models.airquality.MeasuredValue
import fastcampus.aop.part4.chapter06.data.models.monitoringstation.MonitoringStation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {
    /**
     * getNearbyMonitoringStation
     * 사용자 위치에서 가장 가까운 측정소 데이터를 반환
     *
     * 사용자 위치 데이터(WGS84) -> 카카오 좌표 변환 API (WGS84 -> TM)
     * -> 공공데이터 포털에서 가장 가까운 측정소 데이터 반환(주소, 측정소 이름, TM 좌표)
     *
     * @param latitude 사용자 위치 정보 위도값(WGS84)
     * @param longitude 사용자 위치 정보 경도값(WGS84)
     * @return MonitoringStation
     */
    suspend fun getNearbyMonitoringStation(latitude: Double, longitude: Double): MonitoringStation? {
        val tmCoordinates = kakaoLocalApiService
            .getTmCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()
        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y

        return airKoreaApiService
            .getNearbyMonitoringStation(tmX!!, tmY!!)
            .body() //retrofit 응답 body
            ?.response
            ?.body //응답 body 내부에 있는 body
            ?.monitoringStations
            ?.minByOrNull {
                it.tm ?: Double.MAX_VALUE
            } //선택한 요소 중 가장 작은 값을(가장 가까운 측정소 위치정보값을) 반환
    }

    /**
     * getLatestAirQualityData
     * 측정소명을 파라미터로 받아 해당 측정소에서 측정한 가장 최신의 대기질 데이터를 받음
     *
     * @param stationName 사용자와 가장 가까운 측정소명
     * @return MeasuredValue 측정값(co, o3, no2 농도 등 각종 수치)
     */
    suspend fun getLatestAirQualityData(stationName: String): MeasuredValue? =
        airKoreaApiService
            .getRealtimeAirQualities(stationName)
            .body()
            ?.response
            ?.body
            ?.measuredValues
            ?.firstOrNull() //가장 첫번째 데이터를 가져옴. 데이터 없으면 null 반환

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

    /**
     * buildHttpClient
     *
     * @return OkHttpClient
     *
     * okHttp
     * Retrofit 라이브러리를 더 편리하게 쓸 수 있도록 함
     * Http를 더 간편하고 효율적으로 쓸 수 있게 도와주는 클라이언트
     * 입력값이나 서버 응답을 로그로 쉽개 확인하기 위해서, 헤더 값을 편하게 입력하기 위해서 등의 목적으로 사용
     */
    private fun buildHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                /*레벨(level)에 따라 보이는 정보가 달라짐
                  개발 시(디버깅 시)는 로그가 다 보이는 게 좋으나 배포 시에는 로그가 다 보이면 보안 이슈가 발생할 수 있음
                  HttpLoggingInterceptor.Level.BODY :
                  HttpLoggingInterceptor.Level.NONE : */
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
}