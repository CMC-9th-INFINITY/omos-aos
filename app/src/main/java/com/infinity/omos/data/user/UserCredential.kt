package com.infinity.omos.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCredential(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)