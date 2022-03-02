package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class DetailCategory(
    @SerializedName("music") val music: Music,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("recordId") val recordId: String,

    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("liked") val liked: Boolean,
    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("scrapCnt") val scrapCnt: Int,
    @SerializedName("scraped") val scraped: Boolean,
    @SerializedName("viewsCnt") val viewsCnt: Int
)
