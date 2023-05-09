package com.infinity.omos.repository.auth

import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.login.UserEmail
import com.infinity.omos.source.remote.auth.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode> {
        return authRemoteDataSource.sendAuthMail(userEmail)
    }
}