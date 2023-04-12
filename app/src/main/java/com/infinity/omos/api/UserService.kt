package com.infinity.omos.api

import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @POST("api/auth/signup")
    suspend fun signUpUser(
        @Body params: UserSignUp
    ): NetworkResult

}