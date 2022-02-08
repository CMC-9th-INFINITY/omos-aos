package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

/**
 *  수정 필요
 */
data class MyRecord(
    @SerializedName("title") val title: String,
    @SerializedName("contents") val contents: String,
    @SerializedName("music") val music: String,
    @SerializedName("category") val category: String,
    @SerializedName("heart") val heart: String,
    @SerializedName("scrap") val scrap: String
)