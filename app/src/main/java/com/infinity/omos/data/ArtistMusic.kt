package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class ArtistMusic(
    @SerializedName("albumImageUrl") val albumImageUrl: String,
    @SerializedName("artistName") val artistName: List<String>,
    @SerializedName("musicId") val musicId: String,
    @SerializedName("musicTitle") val musicTitle: String
)
