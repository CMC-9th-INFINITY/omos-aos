package com.infinity.omos.data.user.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPassword(
    @SerialName("password") val password: String,
    @SerialName("userId") val userId: Int
)
