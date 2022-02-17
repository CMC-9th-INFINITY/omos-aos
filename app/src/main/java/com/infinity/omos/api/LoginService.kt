package com.infinity.omos.api

import com.infinity.omos.BuildConfig
import com.infinity.omos.data.ResultGetLogin
import com.infinity.omos.data.ResultGetMyRecord
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("movie/upcoming")
    fun getResultGetLogin(
        @Query("id") id: String,
        @Query("pw") pw: String,
    ): Call<ResultGetLogin>
}