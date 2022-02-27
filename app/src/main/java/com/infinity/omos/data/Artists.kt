package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Artists(
    @SerializedName("artistId") val artistId: String,
    @SerializedName("artistName") val artistName: String
)
