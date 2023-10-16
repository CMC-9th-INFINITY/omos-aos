package com.infinity.omos.data.music.album

import com.infinity.omos.data.music.artist.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumMusic(
    @SerialName("artists") val artists: List<Artist>,
    @SerialName("musicId") val musicId: String,
    @SerialName("musicTitle") val musicTitle: String,
)