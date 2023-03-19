package com.infinity.omos.api

import com.infinity.omos.data.user.UserToken
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/reissue")
    fun reissueToken(
        @Body params: UserToken
    ): UserToken
}