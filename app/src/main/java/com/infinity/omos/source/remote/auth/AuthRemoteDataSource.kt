package com.infinity.omos.source.remote.auth

import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.login.UserEmail

interface AuthRemoteDataSource {

    suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode>
}