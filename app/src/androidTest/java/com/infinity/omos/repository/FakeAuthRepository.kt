package com.infinity.omos.repository

import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.login.UserEmail
import com.infinity.omos.repository.auth.AuthRepository
import com.infinity.omos.util.SUCCESS_AUTH_CODE
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    override suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode> {
        return Result.success(AuthCode(SUCCESS_AUTH_CODE))
    }
}