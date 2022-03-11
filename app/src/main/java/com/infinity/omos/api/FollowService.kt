package com.infinity.omos.api

import com.infinity.omos.data.DjProfile
import com.infinity.omos.data.Profile
import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowService {

    @DELETE("api/follow/delete/{fromUserId}/{toUserId}")
    fun deleteFollow(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): Call<ResultState>

    @POST("api/follow/save/{fromUserId}/{toUserId}")
    fun saveFollow(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): Call<ResultState>

    @GET("api/follow/select/{fromUserId}/{toUserId}")
    fun getDjProfile(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): Call<DjProfile>

    @GET("api/follow/select/myDj/{userId}")
    fun getMyDj(
        @Path("userId") userId: Int
    ): Call<List<Profile>>
}