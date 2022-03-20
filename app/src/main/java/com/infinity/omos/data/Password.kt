package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Password(
    @SerializedName("password") val password: String,
    @SerializedName("userId") val userId: Int
)
