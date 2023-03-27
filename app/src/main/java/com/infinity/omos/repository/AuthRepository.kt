package com.infinity.omos.repository

import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.UserEmail
import com.infinity.omos.source.remote.AuthRemoteDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode> {
        return authRemoteDataSource.sendAuthMail(userEmail)
    }
}