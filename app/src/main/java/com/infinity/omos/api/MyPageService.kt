package com.infinity.omos.api

import com.infinity.omos.data.SimpleRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyPageService {
    @GET("/api/records/select/{userId}/liked-records")
    fun getLikeRecord(
        @Path("userId") userId: Int
    ): Call<List<SimpleRecord>>

    @GET("/api/records/select/{userId}/scrapped-records")
    fun getScrapRecord(
        @Path("userId") userId: Int
    ): Call<List<SimpleRecord>>
}