package com.infinity.omos.api

import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ScrapService {
    @DELETE("api/scrap/delete/{postId}/{userId}")
    fun deleteScrap(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<ResultState>

    @POST("api/scrap/save/{postId}/{userId}")
    fun saveScrap(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Call<ResultState>
}