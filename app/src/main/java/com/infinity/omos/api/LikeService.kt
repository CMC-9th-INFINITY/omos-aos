package com.infinity.omos.api

import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeService {
    @DELETE("api/like/delete/{postId}/{userId}")
    fun deleteLike(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<ResultState>

    @POST("api/like/save/{postId}/{userId}")
    fun saveLike(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<ResultState>
}