package com.infinity.omos.data.music.album

import com.infinity.omos.data.music.artist.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("artists") val artists: List<Artist>,
    @SerialName("albumId") val albumId: String,
    @SerialName("albumImageUrl") val albumImageUrl: String,
    @SerialName("albumTitle") val albumTitle: String,
    @SerialName("releaseDate") val releaseDate: String
)