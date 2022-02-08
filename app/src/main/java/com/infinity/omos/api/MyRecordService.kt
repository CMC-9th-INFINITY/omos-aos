package com.infinity.omos.api

import com.infinity.omos.data.ResultGetMyRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  수정 필요
 */
interface MyRecordService {
    @GET("movie/upcoming")
    fun getResultGetMyRecord(
        @Query("api_key") api_key: String = "API KEY 입력",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<ResultGetMyRecord>
}