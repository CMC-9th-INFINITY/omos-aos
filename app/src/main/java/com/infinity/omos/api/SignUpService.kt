package com.infinity.omos.api

import com.infinity.omos.data.ResultState
import com.infinity.omos.data.UserSignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpService {
    @POST("api/auth/signup")
    fun getResultSignUp(
        @Body params: UserSignUp
    ): Call<ResultState>
}