package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class ResultCheckEmail(
    @SerializedName("state") val state: Boolean
)