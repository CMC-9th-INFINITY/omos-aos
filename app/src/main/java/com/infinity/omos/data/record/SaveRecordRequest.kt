package com.infinity.omos.data.record

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveRecordRequest(
    @SerialName("category") val category: String,
    @SerialName("isPublic") val isPublic: Boolean,
    @SerialName("musicId") val musicId: String,
    @SerialName("recordContents") val recordContents: String,
    @SerialName("recordImageUrl") val recordImageUrl: String,
    @SerialName("recordTitle") val recordTitle: String,
    @SerialName("userId") val userId: Int,
)