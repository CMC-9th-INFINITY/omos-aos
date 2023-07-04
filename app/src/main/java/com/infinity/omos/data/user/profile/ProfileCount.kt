package com.infinity.omos.data.user.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileCount(
    @SerialName("followerCount") val followerCount: Int,
    @SerialName("followingCount") val followingCount: Int,
    @SerialName("recordsCount") val recordsCount: Int
)