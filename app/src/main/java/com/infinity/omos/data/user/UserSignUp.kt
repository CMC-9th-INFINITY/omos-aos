package com.infinity.omos.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSignUp(
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("password") val password: String
)