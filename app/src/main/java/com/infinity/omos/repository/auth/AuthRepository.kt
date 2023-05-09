package com.infinity.omos.repository.auth

import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.login.UserEmail

interface AuthRepository {

    suspend fun sendAuthMail(userEmail: UserEmail): Result<AuthCode>
}