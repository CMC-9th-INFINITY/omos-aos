package com.infinity.omos.data.music

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MusicTitle(
    @SerialName("musicTitle") val title: String
)