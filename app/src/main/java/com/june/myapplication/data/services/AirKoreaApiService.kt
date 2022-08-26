package com.june.myapplication.data.services


import com.june.myapplication.BuildConfig
import fastcampus.aop.part4.chapter06.data.models.airquality.AirQualityResponse
import fastcampus.aop.part4.chapter06.data.models.monitoringstation.MonitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApiService {
    //TODO lecture 07:45

    /**
     *
     *
     *
     * 한국환경공단_에어코리아_측정소정보
     * 대기질 측정소 정보를 조회하기 위한 서비스로 TM 좌표기반의 가까운 측정소 및 측정소 목록과 측정소의 정보를 조회할 수 있다.
     * https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15073877
     * 활용기간 2022-08-17 ~ 2024-08-17
     *
     *
     * 한국환경공단_에어코리아_대기오염정보
     * 각 측정소별 대기오염정보를 조회하기 위한 서비스로 기간별, 시도별 대기오염 정보와 통합대기환경지수 나쁨 이상 측정소 내역, 대기질(미세먼지/오존) 예보 통보 내역 등을 조회할 수 있다.
     * https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15073861
     * 활용기간 2022-08-17 ~ 2024-08-17
     *
     * @param tmX 공공데이터포털에서 응답으로 받은 x 좌표
     * @param tmY
     * @return Response<MonitoringStationsResponse>
     *
     */
    @GET("B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}" +
            "&returnType=json")
    suspend fun getNearbyMonitoringStation(
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Response<MonitoringStationsResponse>

    @GET("B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?" +
            "serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}&returnType=json&dataTerm=DAILY&ver=1.3")
    suspend fun getRealtimeAirQualities(
        @Query("stationName") stationName: String
    ): Response<AirQualityResponse>
}
