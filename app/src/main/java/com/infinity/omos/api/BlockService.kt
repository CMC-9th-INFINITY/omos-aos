package com.infinity.omos.api

import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.data.ReportBlock
import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.*

interface BlockService {
    @DELETE("api/block/delete/{fromUserId}/{toUserId}")
    suspend fun cancelBlock(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): ResultState

    @POST("api/block/save/{type}")
    fun blockUser(
        @Body params: ReportBlock,
        @Path("type") type: String
    ): Call<ResultState>

    @GET("api/block/select/{userId}")
    suspend fun getBlockedUsers(
        @Path("userId") userId: Int
    ): List<Profile>
}