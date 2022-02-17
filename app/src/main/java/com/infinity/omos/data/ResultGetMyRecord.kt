package com.infinity.omos.data

import com.google.gson.annotations.SerializedName

/**
 *  API 호출 결과를 담는 DTO
 */
data class ResultGetMyRecord(
    @SerializedName("page") val page: String,
    @SerializedName("results") val myRecordList: List<MyRecord>
)
