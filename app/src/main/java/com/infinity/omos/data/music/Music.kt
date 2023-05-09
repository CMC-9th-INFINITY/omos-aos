package com.infinity.omos.data.music

import com.infinity.omos.data.Artists
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Music(
    @SerialName("musicId") val musicId: String,
    @SerialName("musicTitle") val musicTitle: String,
    @SerialName("artists") val artists: List<Artists>,
    @SerialName("albumTitle") val albumTitle: String,
    @SerialName("albumImageUrl") val albumImageUrl: String
)