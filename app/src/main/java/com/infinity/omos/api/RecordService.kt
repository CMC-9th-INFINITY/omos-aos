package com.infinity.omos.api

import com.infinity.omos.data.*
import retrofit2.Call
import retrofit2.http.*

interface RecordService {
    @PUT("api/records/{postId}/report")
    fun reportRecord(
        @Path("postId") postId: Int
    ): Call<ResultState>

    @GET("api/records/{userId}")
    fun getMyRecord(
        @Path("userId") userId: Int
    ): Call<List<SimpleRecord>>

    @DELETE("api/records/delete/{postId}")
    fun deleteRecord(
        @Path("postId") postId: Int
    ): Call<ResultState>

    @POST("api/records/save")
    fun saveRecord(
        @Body params: SaveRecord
    ): Call<ResultState>

    @GET("api/records/select/{userId}")
    fun setAllRecords(
        @Path("userId") userId: Int
    ): Call<Category>

    @GET("api/records/select/{postId}/user/{userId}")
    fun getDetailRecord(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<Record>

    @GET("api/records/select/{userId}/my-dj")
    fun getMyDjAllRecords(
        @Path("userId") userId: Int,
        @Query("postId") postId: Int?,
        @Query("size") size: Int
    ): Call<List<Record>>

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
        @Query("sortType") sortType: String,
        @Query("userId") userId: Int
    ): Call<List<Record>>

    @GET("api/records/select/user/{fromUserId}/{toUserId}")
    fun getMyDjRecord(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): Call<List<Record>>

    @PUT("api/records/update/{postId}")
    fun updateRecord(
        @Path("postId") postId: Int,
        @Body params: Update
    ): Call<ResultState>
}