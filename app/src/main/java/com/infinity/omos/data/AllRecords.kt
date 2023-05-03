package com.infinity.omos.data

import com.google.gson.annotations.SerializedName
import com.infinity.omos.data.record.SumRecord

data class AllRecords(
    @SerializedName("title") val title: String,
    @SerializedName("category") var category: List<SumRecord>?
)