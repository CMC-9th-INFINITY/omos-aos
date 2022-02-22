package com.infinity.omos.api

import com.infinity.omos.data.UserToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReissueService {
    @POST("api/auth/reissue")
    fun getToken(
        @Body params: UserToken
    ): Call<UserToken>
}