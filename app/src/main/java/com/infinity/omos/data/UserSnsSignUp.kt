package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class UserSnsSignUp(
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("type") val type: String = "KAKAO"
)
