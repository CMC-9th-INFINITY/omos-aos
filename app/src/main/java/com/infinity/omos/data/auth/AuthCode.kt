package com.infinity.omos.data.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthCode(
    @SerialName("code") val code: String
)