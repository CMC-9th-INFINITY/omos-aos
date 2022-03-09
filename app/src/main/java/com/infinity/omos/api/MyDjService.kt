package com.infinity.omos.api

import com.infinity.omos.data.Profile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyDjService {
    @GET("api/follow/select/myDj/{userId}")
    fun getMyDj(
        @Path("userId") userId: Int
    ): Call<List<Profile>>
}