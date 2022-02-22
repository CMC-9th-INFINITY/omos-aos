package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

data class ResultGetMyDj(
    @SerializedName("page") val page: String,
    @SerializedName("results") val myDjList: List<MyDj>
)
