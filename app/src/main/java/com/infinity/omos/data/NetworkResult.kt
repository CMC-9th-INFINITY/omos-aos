package com.infinity.omos.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkResult(
    @SerialName("state") val state: Boolean
)
