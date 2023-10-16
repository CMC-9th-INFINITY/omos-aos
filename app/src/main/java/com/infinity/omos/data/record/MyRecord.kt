package com.infinity.omos.data.record

import com.infinity.omos.data.music.Music
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyRecord(
    @SerialName("category") val category: String,
    @SerialName("createdDate") val createdDate: String,
    @SerialName("isPublic") val isPublic: Boolean,
    @SerialName("music") val music: Music,
    @SerialName("recordContents") val recordContents: String,
    @SerialName("recordId") val recordId: Int,
    @SerialName("recordTitle") val recordTitle: String,
)