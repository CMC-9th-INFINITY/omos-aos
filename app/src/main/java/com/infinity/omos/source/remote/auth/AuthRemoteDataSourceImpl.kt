package com.infinity.omos.source.remote.auth

import com.infinity.omos.api.AuthService
import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.login.UserEmail
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode> {
        return Result.runCatching {
            authService.sendAuthMail(userEmail)
        }
    }
}