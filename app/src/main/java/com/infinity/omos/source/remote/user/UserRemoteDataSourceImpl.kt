package com.infinity.omos.source.remote.user

import com.infinity.omos.api.UserService
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
import com.infinity.omos.data.user.login.*
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {

    override suspend fun loginUser(userCredential: UserCredential): Result<UserToken> {
        return Result.runCatching {
            userService.loginUser(userCredential)
        }
    }

    override suspend fun loginSnsUser(userSnsCredential: UserSnsCredential): Result<UserToken> {
        return Result.runCatching {
            userService.loginSnsUser(userSnsCredential)
        }
    }

    override suspend fun getUserIdFromEmail(email: String): Result<UserId> {
        return Result.runCatching {
            userService.getUserIdFromEmail(email)
        }
    }

    override suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult> {
        return Result.runCatching {
            userService.changePassword(userPassword)
        }
    }

    override suspend fun isNotEmailDuplicate(email: String): Result<NetworkResult> {
        return Result.runCatching {
            userService.isNotEmailDuplicate(email)
        }
    }

    override suspend fun signUpUser(userSignUp: UserSignUp): Result<NetworkResult> {
        return Result.runCatching {
            userService.signUpUser(userSignUp)
        }
    }
}