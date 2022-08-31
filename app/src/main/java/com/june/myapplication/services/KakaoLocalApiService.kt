package com.june.myapplication.services

import com.june.myapplication.BuildConfig
import com.june.myapplication.models.tmcoordinates.TmCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * 카카오 Developers 좌표계 변환하기
 * https://developers.kakao.com/docs/latest/ko/local/dev-guide#trans-coord
 * x, y 값과 입력 및 출력 좌표계를 지정해 변환된 좌표 값을 구한다.
 * 서로 다른 좌표계간 데이터 호환이 가능하도록 한다.
 * 앱 REST API 키를 헤더에 담아 GET으로 요청한다.
 * 좌표와 함께 좌표계 파라미터의 값을 선택해 요청한다.
 *
 * 기본 정보
 * Host: https://dapi.kakao.com
 * GET: /v2/local/geo/transcoord.${FORMAT}
 * Authorization: KakaoAK ${REST_API_KEY}
 *
 * Reqeust_Parameter
 * x: X 좌표값, 경위도인 경우 longitude(경도)
 * y: Y 좌표값, 경위도인 경우 latitude(위도)
 * input_coord: x, y 값의 좌표계
 * output_coord: 변환할 좌표계
 * 지원 좌표계 : WGS84, WCONGNAMUL, CONGNAMUL, WTM, TM, KTM, UTM, BESSEL, WKTM, WUTM
 *
 * GET "https://dapi.kakao.com/v2/local/geo/transcoord.json?x=160710&y=-4388&input_coord=WTM&output_coord=WGS84"
 */
interface KakaoLocalApiService {
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("v2/local/geo/transcoord.json?output_coord=TM")
    suspend fun getTmCoordinates(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ) : Response<TmCoordinatesResponse>

}