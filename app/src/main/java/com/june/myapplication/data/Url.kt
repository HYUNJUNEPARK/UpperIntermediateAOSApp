package com.june.myapplication.data

/**
 * Url 주소가 http 인 경우 추가 요소
 * <uses-permission android:name="android.permission.INTERNET"/>
 *
 * <application
 *    ...
 *      android:usesCleartextTraffic="true"
 *    ...
 */
object Url {
    const val KAKAO_API_BASE_URL = "https://dapi.kakao.com/"
    const val AIR_KOREA_API_BASE_URL = "http://apis.data.go.kr/"
}