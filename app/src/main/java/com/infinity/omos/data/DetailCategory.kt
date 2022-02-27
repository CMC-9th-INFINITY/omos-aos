package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class DetailCategory(
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("likeCnt") val likeCnt: Int,
    @SerializedName("liked") val liked: Boolean,
    @SerializedName("albumImageUrl") val albumImageUrl: String,
    @SerializedName("albumTitle") val albumTitle: String,
    @SerializedName("artists") val artists: List<Artists>,
    @SerializedName("musicId") val musicId: String,
    @SerializedName("musicTitle") val musicTitle: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("recordId") val recordId: String,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("scrapCnt") val scrapCnt: Int,
    @SerializedName("scraped") val scraped: Boolean,
    @SerializedName("viewsCnt") val viewsCnt: Int,
    @SerializedName("userId") val userId: Int
)
