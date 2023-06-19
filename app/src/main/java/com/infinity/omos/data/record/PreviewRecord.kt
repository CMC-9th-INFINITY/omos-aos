package com.infinity.omos.data.record

import com.infinity.omos.data.music.Music
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewRecord(
    @SerialName("music") val music: Music,
    @SerialName("nickname") val nickname: String,
    @SerialName("recordId") val recordId: Int,
    @SerialName("recordImageUrl") val recordImageUrl: String,
    @SerialName("recordTitle") val recordTitle: String,
    @SerialName("userId") val userId: Int
)