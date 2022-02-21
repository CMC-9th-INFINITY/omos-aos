package com.infinity.omos.api

import com.infinity.omos.data.ResultUserInfo
import com.infinity.omos.data.UserSnsSignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SnsSignUpService {
    @POST("api/auth/sns-signup")
    fun getResultSnsSignUp(
        @Body params: UserSnsSignUp
    ): Call<ResultUserInfo>
}