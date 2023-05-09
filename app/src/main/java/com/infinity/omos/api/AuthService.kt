package com.infinity.omos.api

import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.login.UserEmail
import com.infinity.omos.data.user.UserToken
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/reissue")
    suspend fun reissueToken(
        @Body params: UserToken
    ): UserToken

    @POST("api/auth/email")
    suspend fun sendAuthMail(
        @Body params: UserEmail
    ): AuthCode
}