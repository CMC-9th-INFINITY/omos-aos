package com.infinity.omos.api

import com.infinity.omos.data.*
import retrofit2.Call
import retrofit2.http.*

interface MyPageService {
    @GET("/api/records/select/{userId}/my-records")
    fun getMyPageData(
        @Path("userId") userId: Int
    ): Call<MyPage>

    @GET("/api/records/select/{userId}/liked-records")
    fun getLikeRecord(
        @Path("userId") userId: Int
    ): Call<List<SimpleRecord>>

    @GET("/api/records/select/{userId}/scrapped-records")
    fun getScrapRecord(
        @Path("userId") userId: Int
    ): Call<List<SimpleRecord>>

    @PUT("/api/user/update/profile")
    fun updateProfile(
        @Body params: Profile
    ): Call<ResultState>

    @PUT("/api/user/update/password")
    fun updatePassword(
        @Body params: Password
    ): Call<ResultState>

    @DELETE("/api/auth/logout/{userId}")
    fun doLogout(
        @Path("userId") userId: Int
    ): Call<ResultState>
}