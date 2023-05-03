package com.infinity.omos.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Music(
    @SerialName("musicId") val musicId: String,
    @SerialName("musicTitle") val musicTitle: String,
    @SerialName("artists") val artists: List<Artists>,
    @SerialName("albumId") val albumId: String,
    @SerialName("albumTitle") val albumTitle: String,
    @SerialName("albumImageUrl") val albumImageUrl: String
)