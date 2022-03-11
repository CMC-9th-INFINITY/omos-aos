package com.infinity.omos.api

import com.infinity.omos.data.Profile
import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface LikeService {
    @DELETE("api/like/delete/{postId}/{userId}")
    fun deleteLike(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<ResultState>
}