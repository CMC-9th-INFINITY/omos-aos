package com.infinity.omos.api

import com.infinity.omos.data.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("api/user/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Int
    ): Profile
}