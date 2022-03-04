package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("artists") val artists: List<Artists>,
    @SerializedName("albumId") val albumId: String,
    @SerializedName("albumImageUrl") val albumImageUrl: String,
    @SerializedName("albumTitle") val albumTitle: String,
    @SerializedName("releaseDate") val releaseDate: String
)