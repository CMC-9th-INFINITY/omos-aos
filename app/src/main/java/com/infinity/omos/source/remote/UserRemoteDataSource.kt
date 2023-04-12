package com.infinity.omos.source.remote

import com.infinity.omos.api.UserService
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
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

    suspend fun getUserIdFromEmail(email: String): Result<UserId> {
        return Result.runCatching {
            userService.getUserIdFromEmail(email)
        }
    }

    suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult> {
        return Result.runCatching {
            userService.changePassword(userPassword)
        }
    }

    suspend fun signUpUser(userSignUp: UserSignUp): Result<NetworkResult> {
        return Result.runCatching {
            userService.signUpUser(userSignUp)
        }
    }
}