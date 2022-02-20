package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class ResponseErrorBody(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String
)
