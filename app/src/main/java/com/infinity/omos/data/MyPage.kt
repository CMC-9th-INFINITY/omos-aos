package com.infinity.omos.data

import com.google.gson.annotations.SerializedName
import com.infinity.omos.data.record.PreviewRecord

data class MyPage(
    @SerializedName("likedRecords") val likedRecords: List<PreviewRecord>,
    @SerializedName("scrappedRecords") val scrappedRecords: List<PreviewRecord>
)
