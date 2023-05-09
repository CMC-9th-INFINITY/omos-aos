package com.infinity.omos.data

import com.google.gson.annotations.SerializedName
import com.infinity.omos.data.user.Profile

data class DjProfile(
    @SerializedName("count") val count: Count,
    @SerializedName("isFollowed") val isFollowed: Boolean,
    @SerializedName("profile") val profile: Profile
)
