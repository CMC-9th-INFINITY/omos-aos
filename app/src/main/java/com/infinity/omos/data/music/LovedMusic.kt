package com.infinity.omos.data.music

import com.infinity.omos.data.music.Music
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LovedMusic(
    @SerialName("recordId") val recordId: Int,
    @SerialName("music") val music: Music,
    @SerialName("recordImageUrl") val recordImageUrl: String
)