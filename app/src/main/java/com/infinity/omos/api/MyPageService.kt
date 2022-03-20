package com.infinity.omos.api

import com.infinity.omos.data.Password
import com.infinity.omos.data.Profile
import com.infinity.omos.data.ResultState
import com.infinity.omos.data.SimpleRecord
import retrofit2.Call
import retrofit2.http.*

interface MyPageService {
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