package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("userId") val userId: Long
)
