package com.infinity.omos.api

import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
import com.infinity.omos.data.user.login.*
import retrofit2.http.*

interface UserService {

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body params: UserCredential
    ): UserToken

    @POST("api/auth/sns-login")
    suspend fun loginSnsUser(
        @Body params: UserSnsCredential
    ): UserToken

    @GET("api/auth/{email}")
    suspend fun getUserIdFromEmail(
        @Path("email") email: String
    ): UserId

    @PUT("api/auth/update/password")
    suspend fun changePassword(
        @Body params: UserPassword
    ): NetworkResult

    @GET("api/auth/check-email")
    suspend fun isNotEmailDuplicate(
        @Query("email") email: String
    ): NetworkResult

    @POST("api/auth/signup")
    suspend fun signUpUser(
        @Body params: UserSignUp
    ): NetworkResult
}