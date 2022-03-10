package com.infinity.omos.api

import com.infinity.omos.data.Record
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailRecordService {
    @GET("api/records/select/{postId}/user/{userId}")
    fun getDetailRecord(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<Record>
}