package com.infinity.omos.api

import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserToken
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body params: UserCredential
    ): UserToken
}