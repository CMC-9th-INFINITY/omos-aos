package com.infinity.omos.api

import com.infinity.omos.BuildConfig
import com.infinity.omos.data.ResultGetMyDj
import com.infinity.omos.data.ResultGetMyRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyDjService {
    @GET("movie/upcoming")
    fun getResultMyDj(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") api_key: String = BuildConfig.RECORD_API_KEY
    ): Call<ResultGetMyDj>
}