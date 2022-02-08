package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

/**
 *  수정 필요
 */
data class ResultGetMyRecord(
    @SerializedName("page") val page: String,
    @SerializedName("results") val myRecordList: List<MyRecord>
)
