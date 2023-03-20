package com.infinity.omos.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSnsCredential(
    @SerialName("email") val email: String,
    @SerialName("type") val type: String
)