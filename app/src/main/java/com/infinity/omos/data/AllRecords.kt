package com.infinity.omos.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllRecords(
    @SerializedName("title") val title: String,
    @SerializedName("category") var category: List<DetailCategory>?
)