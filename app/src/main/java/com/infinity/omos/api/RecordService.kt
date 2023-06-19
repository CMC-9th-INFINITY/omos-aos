package com.infinity.omos.api

import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.MyRecord
import retrofit2.http.GET
import retrofit2.http.Path

interface RecordService {

    @GET("/api/records/{userId}")
    suspend fun getMyRecords(
        @Path("userId") userId: Int
    ): List<MyRecord>

    @GET("/api/records/select/{userId}")
    suspend fun getAllRecords(
        @Path("userId") userId: Int
    ): AllRecords
}