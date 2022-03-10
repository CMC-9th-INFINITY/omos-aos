package com.infinity.omos.api

import com.infinity.omos.data.Category
import com.infinity.omos.data.Record
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.SaveRecord
import retrofit2.Call
import retrofit2.http.*

interface RecordService {
    @GET("api/records/{userId}")
    fun getMyRecord(
        @Path("userId") userId: Int
    ): Call<List<Record>>

    @POST("api/records/save")
    fun saveRecord(
        @Body params: SaveRecord
    ): Call<ResultState>

    @GET("api/records/select")
    fun setAllRecords(): Call<Category>

    @GET("api/records/select/{postId}/user/{userId}")
    fun getDetailRecord(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<Record>

    @GET("api/records/select/category/{category}")
    fun getCategory(
        @Path("category") category: String,
        @Query("postId") postId: Int?,
        @Query("size") size: Int,
        @Query("sortType") sortType: String,
        @Query("userid") userid: Int
    ): Call<List<Record>>

    @GET("api/records/select/music/{musicId}")
    fun getMusicRecord(
        @Path("musicId") musicId: String,
        @Query("postId") postId: Int?,
        @Query("size") size: Int,
        @Query("userId") userId: Int
    ): Call<List<Record>>

    @GET("api/records/select/user/{fromUserId}/{toUserId}")
    fun getMyDjRecord(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): Call<List<Record>>
}