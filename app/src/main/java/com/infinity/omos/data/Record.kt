package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("category") val category: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("isLiked") val isLiked: Boolean,
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("isScraped") val isScraped: Boolean,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("music") val music: Music,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("recordImageUrl") val recordImageUrl: String,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("scrapCnt") val scrapCnt: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("viewsCnt") val viewsCnt: Int
)
