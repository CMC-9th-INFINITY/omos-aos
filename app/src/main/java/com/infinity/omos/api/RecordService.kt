package com.infinity.omos.api

import com.infinity.omos.data.record.MyPageRecord
import com.infinity.omos.data.record.AllRecords
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.record.MyRecord
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordService {

    @GET("/api/records/{userId}")
    suspend fun getMyRecords(
        @Path("userId") userId: Int
    ): List<MyRecord>

    @GET("/api/records/select/{userId}")
    suspend fun getAllRecords(
        @Path("userId") userId: Int
    ): AllRecords

    @GET("api/records/select/{userId}/my-dj")
    suspend fun getDetailRecords(
        @Path("userId") userId: Int,
        @Query("postId") postId: Int?,
        @Query("size") size: Int
    ): List<DetailRecord>

    @GET("api/records/select/user/{fromUserId}/{toUserId}")
    suspend fun getDetailRecords(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): List<DetailRecord>

    @GET("/api/records/select/{userId}/my-records")
    suspend fun getMyPageRecords(
        @Path("userId") userId: Int
    ): MyPageRecord
}