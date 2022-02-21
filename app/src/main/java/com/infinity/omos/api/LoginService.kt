package com.infinity.omos.api

import com.infinity.omos.data.ResultUserInfo
import com.infinity.omos.data.UserLogin
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("api/auth/login")
    fun getResultLogin(
        @Body params: UserLogin
    ): Call<ResultUserInfo>
}