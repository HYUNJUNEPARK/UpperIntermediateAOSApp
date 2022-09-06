package com.june.myapplication.models.airquality


import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("response")
    val response: Response? = null
)
