package com.infinity.omos.data.record

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllRecords(
    @SerialName("STORY") val story: List<PreviewRecord>,
    @SerialName("A_LINE") val a_line: List<PreviewRecord>,
    @SerialName("LYRICS") val lyrics: List<PreviewRecord>,
    @SerialName("OST") val ost: List<PreviewRecord>,
    @SerialName("FREE") val free: List<PreviewRecord>
)