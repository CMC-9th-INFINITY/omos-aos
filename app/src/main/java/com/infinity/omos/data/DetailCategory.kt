package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class DetailCategory(
    @SerializedName("music") val music: Music,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("recordId") val recordId: String,

    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("category") val category: String,
    @SerializedName("viewsCnt") val viewsCnt: Int,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("scrapCnt") val scrapCnt: Int,
    @SerializedName("isLiked") val isLiked: Boolean,
    @SerializedName("isScraped") val isScraped: Boolean,
)
