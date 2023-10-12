package com.infinity.omos.data.music.artist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("artistId") val artistId: String,
    @SerialName("artistImageUrl") val artistImageUrl: String? = "",
    @SerialName("artistName") val artistName: String,
    @SerialName("genres") val genres: List<String> = emptyList(),
)