package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class UserSNSLogin(
    @SerializedName("email") val email: String,
    @SerializedName("type") val type: String = "KAKAO"
)