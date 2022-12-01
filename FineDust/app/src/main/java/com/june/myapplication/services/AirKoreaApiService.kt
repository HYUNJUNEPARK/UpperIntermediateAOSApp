package com.june.myapplication.services

import com.june.myapplication.BuildConfig
import com.june.myapplication.models.airquality.AirQualityResponse
import com.june.myapplication.models.monitoringstation.MonitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Base Url 주소가 http 인 경우
 * AndroidManifest.xml
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <application
 *      android:usesCleartextTraffic="true">
 */
interface AirKoreaApiService {
    /**
     * 한국환경공단_에어코리아_측정소정보
     * https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15073877
     * TM 좌표를 입력하여 입력된 좌표 주변 측정소 정보와 입력 좌표와의 거리 조회 기능 제공한다.
     *
     * 기본 정보
     * Host: http://apis.data.go.kr
     * GET: /B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList
     * ```
     * Request_Parameter
     * serviceKey: 서비스키
     * returnType: 데이터 표출방식(옵션 사항, xml, json)
     * tmX: TM 측정방식 X좌표
     * tmY: TM 측정방식 Y좌표
     * ver: 오퍼레이션 버전(옵션 사항, 상세내용은 문서 참고)
     * ```
     * GET "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList
     * ?serviceKey=서비스키&tmX=123&tmY=222&returnType=json"
     */
    @GET("B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}&returnType=json")
    suspend fun getNearbyMonitoringStation(
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Response<MonitoringStationsResponse>

    /**
     * 한국환경공단_에어코리아_대기오염정보
     * https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15073861
     * 측정소별 실시간 측정정보 조회한다.
     *
     * 기본 정보
     * Host: http://apis.data.go.kr
     * GET: /B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty
     * ```
     * Reqeust_Parameter
     * serviceKey: 서비스키
     * stationName: 측정소명
     * dataTerm: 데이터기간(daily, month, 3month)
     * returnType: 데이터 표출방식(옵션 사항)
     * numOfRows: 한 페이지 결과 수(옵션 사항)
     * pageNo: 페이지 번호(옵션 사항)
     * ver: 오페레이션 버전(옵션 사항)
     * ```
     * GET http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty
     * ?stationName=종로구&dataTerm=month&pageNo=1&numOfRows=100&returnType=xml&serviceKey=서비스키
     */
    @GET("B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}&returnType=json&dataTerm=DAILY&ver=1.3")
    suspend fun getRealtimeAirQualities(
        @Query("stationName") stationName: String
    ): Response<AirQualityResponse>
}