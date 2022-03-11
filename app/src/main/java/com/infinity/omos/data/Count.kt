package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("followerCount") val followerCount: String,
    @SerializedName("followingCount") val followingCount: String,
    @SerializedName("recordsCount") val recordsCount: String
)