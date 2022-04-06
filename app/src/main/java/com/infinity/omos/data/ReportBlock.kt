package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class ReportBlock(
    @SerializedName("fromUserId") val fromUserId: Int,
    @SerializedName("recordId") val recordId: Int?,
    @SerializedName("reportReason") val reportReason: String?,
    @SerializedName("toUserId") val toUserId: Int?
)
