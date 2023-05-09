package com.infinity.omos.data.user.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEmail(
    @SerialName("email") val email: String
)