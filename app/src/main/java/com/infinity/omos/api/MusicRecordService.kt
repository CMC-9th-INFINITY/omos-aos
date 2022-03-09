package com.infinity.omos.api

import com.infinity.omos.data.Record
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicRecordService {
    @GET("api/records/select/{musicId}")
    fun getMusicRecord(
        @Path("musicId") musicId: String,
        @Query("postId") postId: Int?,
        @Query("size") size: Int,
        @Query("userId") userId: Int
    ): Call<List<Record>>
}