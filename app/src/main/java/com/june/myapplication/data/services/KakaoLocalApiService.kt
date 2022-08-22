package com.june.myapplication.data.services

import com.june.myapplication.BuildConfig
import com.june.myapplication.data.models.tmcoordinates.TmCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * https://developers.kakao.com/docs/latest/ko/local/dev-guide#coord-to-address
 *
 * Request
 * a) URL
 * GET /v2/local/geo/coord2address.${FORMAT} HTTP/1.1
 * Host: dapi.kakao.com
 * Authorization: KakaoAK ${REST_API_KEY}
 * b)Parameter
 * output_coord: String : 변환할 좌표계
 */
interface KakaoLocalApiService {
    @Headers("Authorization: KaKaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("v2/local/geo/coord2address.jason?=output_coord=TM")
    suspend fun getTmCoordinates(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ) : Response<TmCoordinatesResponse>

}