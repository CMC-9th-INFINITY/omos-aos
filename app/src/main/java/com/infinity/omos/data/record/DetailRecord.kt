package com.infinity.omos.data.record

import com.infinity.omos.data.music.Music
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailRecord(
    @SerialName("category") val category: String,
    @SerialName("createdDate") val createdDate: String,
    @SerialName("isLiked") val isLiked: Boolean,
    @SerialName("isPublic") val isPublic: Boolean = true,
    @SerialName("isScraped") val isScraped: Boolean,
    @SerialName("likeCnt") val likeCnt: Int,
    @SerialName("music") val music: Music,
    @SerialName("nickname") val nickname: String,
    @SerialName("recordContents") val recordContents: String,
    @SerialName("recordId") val recordId: Int,
    @SerialName("recordImageUrl") val recordImageUrl: String,
    @SerialName("recordTitle") val recordTitle: String,
    @SerialName("scrapCnt") val scrapCnt: Int,
    @SerialName("userId") val userId: Int
)