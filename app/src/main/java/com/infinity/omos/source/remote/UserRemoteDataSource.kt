package com.infinity.omos.source.remote

import com.infinity.omos.api.UserService
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserToken
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
) {

    suspend fun loginUser(userCredential: UserCredential): Result<UserToken> {
        return Result.runCatching {
            userService.loginUser(userCredential)
        }
    }
}