package com.infinity.omos.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("nickname") val nickname: String,
    @SerialName("profileUrl") val profileUrl: String?,
    @SerialName("userId") val userId: Int
)