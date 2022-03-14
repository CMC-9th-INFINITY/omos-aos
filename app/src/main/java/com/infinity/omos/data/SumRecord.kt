package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class SumRecord(
    @SerializedName("music") val music: Music,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("recordImageUrl") val recordImageUrl: String,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("userId") val userId: Int
)