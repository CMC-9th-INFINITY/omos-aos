package com.infinity.omos.data

import com.infinity.omos.data.record.PreviewRecord
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FakeAllRecords(
    @SerialName("title") val title: String,
    @SerialName("category") var category: List<PreviewRecord>?
)