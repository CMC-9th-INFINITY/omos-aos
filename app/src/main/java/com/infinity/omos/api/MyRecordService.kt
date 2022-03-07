package com.infinity.omos.api

import com.infinity.omos.data.MyRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *  수정 필요
 */
interface MyRecordService {
    @GET("api/records/{userId}")
    fun getMyRecord(
        @Path("userId") userId: Int
    ): Call<List<MyRecord>>
}