package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class AllRecords(
    @SerializedName("title") val title: String,
    @SerializedName("myRecord") var myRecord: List<MyRecord>?
)