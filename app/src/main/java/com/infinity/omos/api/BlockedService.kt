package com.infinity.omos.api

import com.infinity.omos.data.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface BlockedService {
    @GET("api/block/select/{userId}")
    suspend fun getBlockedUsers(
        @Path("userId") userId: Int
    ): List<Profile>
}