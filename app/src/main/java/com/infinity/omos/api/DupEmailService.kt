package com.infinity.omos.api

import com.infinity.omos.data.ResultState
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DupEmailService {
    @GET("api/auth/check-email")
    fun checkDupEmail(
        @Query("email") email: String
    ): Call<ResultState>
}