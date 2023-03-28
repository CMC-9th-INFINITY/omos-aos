package com.infinity.omos.source.remote

import com.infinity.omos.api.UserService
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserEmail
import com.infinity.omos.data.user.UserPassword
import com.infinity.omos.data.user.UserSnsCredential
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

    suspend fun loginSnsUser(userSnsCredential: UserSnsCredential): Result<UserToken> {
        return Result.runCatching {
            userService.loginSnsUser(userSnsCredential)
        }
    }

    suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult> {
        return Result.runCatching {
            userService.changePassword(userPassword)
        }
    }
}