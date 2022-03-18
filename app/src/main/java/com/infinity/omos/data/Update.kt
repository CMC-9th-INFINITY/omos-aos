package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Update(
    @SerializedName("contents") val contents: String,
    @SerializedName("isPublic") val isPublic: Boolean,
    @SerializedName("recordImageUrl") val recordImageUrl: String,
    @SerializedName("title") val title: String
)
