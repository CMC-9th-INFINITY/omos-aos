package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class SaveRecord(
    @SerializedName("category") val category: String,
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("musicId") val musicId: String,
    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("recordImageUrl") val recordImageUrl: String,
    @SerializedName("recordTitle") val recordTitle: String,
    @SerializedName("userId") val userId: Int
)