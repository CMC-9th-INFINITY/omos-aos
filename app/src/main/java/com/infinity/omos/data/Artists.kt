package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class Artists(
    @SerializedName("artistId") val artistId: String,
    @SerializedName("artistImageUrl") val artistImageUrl: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("genres") val genres: List<String>,
)
