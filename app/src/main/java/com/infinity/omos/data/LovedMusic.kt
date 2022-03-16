package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class LovedMusic(
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("music") val music: Music,
    @SerializedName("recordImageUrl") val recordImageUrl: String
)