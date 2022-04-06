package com.infinity.omos.api

import com.infinity.omos.data.ReportBlock
import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface BlockService {
    @DELETE("api/block/delete/{blockId}")
    fun cancelBlock(
        @Path("blockId") blockId: Int
    ): Call<ResultState>

    @POST("api/block/save/{type}")
    fun blockUser(
        @Body params: ReportBlock,
        @Path("type") type: String
    ): Call<ResultState>
}