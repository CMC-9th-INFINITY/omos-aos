package com.infinity.omos.api

import com.infinity.omos.data.user.profile.DjProfile
import com.infinity.omos.data.user.profile.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface DjService {

    @GET("api/follow/select/{userId}/follower")
    suspend fun getFollower(
        @Path("userId") userId: Int
    ): List<Profile>

    @GET("api/follow/select/{userId}/following")
    suspend fun getFollowing(
        @Path("userId") userId: Int
    ): List<Profile>

    @GET("api/follow/select/myDj/{userId}")
    suspend fun getMyDjs(
        @Path("userId") userId: Int
    ): List<Profile>

    @GET("api/follow/select/{fromUserId}/{toUserId}")
    suspend fun getDjProfile(
        @Path("fromUserId") fromUserId: Int,
        @Path("toUserId") toUserId: Int
    ): DjProfile
}