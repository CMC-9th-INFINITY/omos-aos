package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class DetailCategory(
    @SerializedName("albumImageUrl") val albumImageUrl: String,
    @SerializedName("albumTitle") val albumTitle: String,
    @SerializedName("artists") val artists: List<Artists>,
    @SerializedName("musicId") val musicId: String,
    @SerializedName("musicTitle") val musicTitle: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("recordId") val recordId: String,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("userId") val userId: String
)
