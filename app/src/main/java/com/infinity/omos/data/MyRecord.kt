package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

/**
 *  수정 필요
 */
data class MyRecord(
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val contents: String,
    @SerializedName("overview") val category: String
)