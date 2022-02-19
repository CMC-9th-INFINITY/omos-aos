package com.infinity.omos.api

import com.infinity.omos.BuildConfig
import com.infinity.omos.data.ResultGetLogin
import com.infinity.omos.data.ResultGetMyRecord
import com.infinity.omos.data.UserLogin
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("api/auth/login")
    fun getResultGetLogin(
        @Body params: UserLogin
    ): Call<ResultGetLogin>
}