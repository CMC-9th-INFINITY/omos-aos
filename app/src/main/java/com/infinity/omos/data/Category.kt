package com.infinity.omos.data

import com.google.gson.annotations.SerializedName
import com.infinity.omos.data.record.PreviewRecord

data class Category(
    @SerializedName("STORY") val story: List<PreviewRecord>?,
    @SerializedName("A_LINE") val a_line: List<PreviewRecord>?,
    @SerializedName("LYRICS") val lyrics: List<PreviewRecord>?,
    @SerializedName("OST") val ost: List<PreviewRecord>?,
    @SerializedName("FREE") val free: List<PreviewRecord>?
)
