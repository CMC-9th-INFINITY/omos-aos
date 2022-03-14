package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("STORY") val story: List<SumRecord>?,
    @SerializedName("A_LINE") val a_line: List<SumRecord>?,
    @SerializedName("LYRICS") val lyrics: List<SumRecord>?,
    @SerializedName("OST") val ost: List<SumRecord>?,
    @SerializedName("FREE") val free: List<SumRecord>?
)
