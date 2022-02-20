package com.infinity.omos.api

import com.infinity.omos.data.ResultGetLogin
import com.infinity.omos.data.UserLogin
import com.infinity.omos.data.UserSNSLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SnsLoginService {
    @POST("api/auth/sns-login")
    fun getResultSnsLogin(
        @Body params: UserSNSLogin
    ): Call<ResultGetLogin>
}