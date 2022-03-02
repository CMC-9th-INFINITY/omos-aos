package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("artists") val artists: List<Artists>,
    @SerializedName("albumId") val albumId: String,
    @SerializedName("albumImageUrl") val albumImageUrl: Artists,
    @SerializedName("albumTitle") val albumTitle: Artists,
    @SerializedName("releaseDate") val releaseDate: Artists
)