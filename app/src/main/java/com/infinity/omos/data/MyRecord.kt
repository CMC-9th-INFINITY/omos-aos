package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class MyRecord(
    @SerializedName("category") val category: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("music") val music: Music,
    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("recordImageUrl") val recordImageUrl: String,
    @SerializedName("recordTitle") val recordTitle: String,

    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("musicId") val musicId: String,
    @SerializedName("userId") val userId: Int
)