package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("STORY") val story: List<DetailCategory>?,
    @SerializedName("A_LINE") val a_line: List<DetailCategory>?,
    @SerializedName("LYRICS") val lyrics: List<DetailCategory>?,
    @SerializedName("OST") val ost: List<DetailCategory>?,
    @SerializedName("FREE") val free: List<DetailCategory>?
)
