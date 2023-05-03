package com.infinity.omos.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artists(
    @SerialName("artistId") val artistId: String,
    @SerialName("artistImageUrl") val artistImageUrl: String,
    @SerialName("artistName") val artistName: String,
    @SerialName("genres") val genres: List<String>,
)
