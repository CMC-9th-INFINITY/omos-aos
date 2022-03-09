package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("profileUrl") val profileUrl: String,
    @SerializedName("userId") val userId: Int
)
