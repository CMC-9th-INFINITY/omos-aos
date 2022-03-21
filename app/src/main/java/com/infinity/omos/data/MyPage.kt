package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class MyPage(
    @SerializedName("likedRecords") val likedRecords: List<SumRecord>,
    @SerializedName("scrappedRecords") val scrappedRecords: List<SumRecord>
)
