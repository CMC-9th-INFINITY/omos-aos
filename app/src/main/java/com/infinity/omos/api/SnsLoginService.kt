package com.infinity.omos.api

import com.infinity.omos.data.UserToken
import com.infinity.omos.data.UserSnsLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SnsLoginService {
    @POST("api/auth/sns-login")
    fun getResultSnsLogin(
        @Body params: UserSnsLogin
    ): Call<UserToken>
}