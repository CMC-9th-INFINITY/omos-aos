package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class SimpleRecord(
    @SerializedName("category") val category: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("isPublic") val isPublic: Boolean? = null,
    @SerializedName("music") val music: Music,
    @SerializedName("recordContents") val recordContents: String,
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("recordTitle") val recordTitle: String
)