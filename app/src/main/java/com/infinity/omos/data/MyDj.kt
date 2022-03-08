package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class MyDj(
    @SerializedName("title") var nickname: String,
    @SerializedName("original_title") val profile: String,
    @SerializedName("myRecord") var records: List<SaveRecord>?
)
