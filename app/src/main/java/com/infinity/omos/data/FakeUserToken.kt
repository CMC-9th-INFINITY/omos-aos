package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class FakeUserToken(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("userId") val userId: Int
)
