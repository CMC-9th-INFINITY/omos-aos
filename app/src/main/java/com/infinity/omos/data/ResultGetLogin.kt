package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class ResultGetLogin(
    @SerializedName("results") val isExist: Boolean
)
