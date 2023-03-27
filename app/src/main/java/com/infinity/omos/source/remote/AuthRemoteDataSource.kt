package com.infinity.omos.source.remote

import com.infinity.omos.api.AuthService
import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.UserEmail
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {

    suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode> {
        return Result.runCatching {
            authService.sendAuthMail(userEmail)
        }
    }
}