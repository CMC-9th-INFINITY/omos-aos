package com.infinity.omos.repository

import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
import com.infinity.omos.repository.user.UserRepository
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    override suspend fun loginUser(userCredential: UserCredential): Result<UserToken> {
        return Result.success(UserToken("", "", 0))
    }

    override suspend fun loginSnsUser(userSnsCredential: UserSnsCredential): Result<UserToken> {
        return Result.success(UserToken("", "", 0))
    }

    override suspend fun getUserIdFromEmail(email: String): Result<UserId> {
        return Result.success(UserId(0))
    }

    override suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult> {
        return Result.success(NetworkResult(true))
    }

    override suspend fun isNotEmailDuplicate(email: String): Result<NetworkResult> {
        return Result.success(NetworkResult(true))
    }

    override suspend fun signUpUser(userSignUp: UserSignUp): Result<NetworkResult> {
        return Result.success(NetworkResult(true))
    }

    override suspend fun saveToken(token: UserToken) = Unit
}