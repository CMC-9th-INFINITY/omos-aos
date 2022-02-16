package com.infinity.omos.api

import com.infinity.omos.BuildConfig
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
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") api_key: String = BuildConfig.RECORD_API_KEY
    ): Call<ResultGetMyRecord>
}