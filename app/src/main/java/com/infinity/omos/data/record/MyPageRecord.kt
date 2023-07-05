package com.infinity.omos.data.record

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyPageRecord(
    @SerialName("likedRecords") val likedRecords: List<PreviewRecord>,
    @SerialName("scrappedRecords") val scrappedRecords: List<PreviewRecord>
)