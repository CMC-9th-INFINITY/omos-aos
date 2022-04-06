package com.infinity.omos.api

import com.infinity.omos.data.*
import retrofit2.Call
import retrofit2.http.*

interface AuthService {
    @GET("api/auth/check-email")
    fun checkDupEmail(
        @Query("email") email: String
    ): Call<ResultState>

    @POST("api/auth/email")
    fun getEmailCode(
        @Body params: Email
    ): Call<Code>

    @POST("api/auth/login")
    fun getResultLogin(
        @Body params: UserLogin
    ): Call<UserToken>

    @POST("api/auth/reissue")
    fun getToken(
        @Body params: UserToken
    ): Call<UserToken>

    @POST("api/auth/signup")
    fun getResultSignUp(
        @Body params: UserSignUp
    ): Call<ResultState>

    @POST("api/auth/sns-login")
    fun getResultSnsLogin(
        @Body params: UserSnsLogin
    ): Call<UserToken>

    @POST("api/auth/sns-signup")
    fun getResultSnsSignUp(
        @Body params: UserSnsSignUp
    ): Call<UserToken>

    @PUT("/api/auth/update/password")
    fun updatePassword(
        @Body params: Password
    ): Call<ResultState>

    @GET("api/auth/{email}")
    fun getUserIdFromEmail(
        @Path("email") email: String
    ): Call<UserId>
}