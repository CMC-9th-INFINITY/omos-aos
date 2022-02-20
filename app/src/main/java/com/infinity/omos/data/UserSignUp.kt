package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class UserSignUp(
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("password") val password: String
)
