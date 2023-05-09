package com.infinity.omos.api

import com.infinity.omos.data.user.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeUserService {
    @GET("api/user/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): Profile
}