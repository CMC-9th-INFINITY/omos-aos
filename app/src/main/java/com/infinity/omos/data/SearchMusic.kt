package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class SearchMusic(
    @SerializedName("musicTitle") val musicTitle: String
)