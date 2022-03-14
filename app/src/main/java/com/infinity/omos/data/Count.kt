package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("followerCount") val followerCount: Int,
    @SerializedName("followingCount") val followingCount: Int,
    @SerializedName("recordsCount") val recordsCount: Int
)