package com.june.myapplication.models.airquality


import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("resultCode")
    val resultCode: String? = null,
    @SerializedName("resultMsg")
    val resultMsg: String? = null
)
