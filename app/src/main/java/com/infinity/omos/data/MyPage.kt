package com.infinity.omos.data

import com.google.gson.annotations.SerializedName
import com.infinity.omos.data.record.SumRecord

data class MyPage(
    @SerializedName("likedRecords") val likedRecords: List<SumRecord>,
    @SerializedName("scrappedRecords") val scrappedRecords: List<SumRecord>
)
