package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("STORY") val story: List<Record>?,
    @SerializedName("A_LINE") val a_line: List<Record>?,
    @SerializedName("LYRICS") val lyrics: List<Record>?,
    @SerializedName("OST") val ost: List<Record>?,
    @SerializedName("FREE") val free: List<Record>?
)
