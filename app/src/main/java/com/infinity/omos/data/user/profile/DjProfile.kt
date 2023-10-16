package com.infinity.omos.data.user.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DjProfile(
    @SerialName("count") val count: ProfileCount,
    @SerialName("isFollowed") val isFollowed: Boolean? = null,
    @SerialName("profile") val profile: Profile
)