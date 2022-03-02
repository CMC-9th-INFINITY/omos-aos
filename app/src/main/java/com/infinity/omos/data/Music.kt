package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Music(
    @SerializedName("musicId") val musicId: String,
    @SerializedName("musicTitle") val musicTitle: String,
    @SerializedName("artists") val artists: List<Artists>,
    @SerializedName("albumTitle") val albumTitle: String,
    @SerializedName("albumImageUrl") val albumImageUrl: String,
)
