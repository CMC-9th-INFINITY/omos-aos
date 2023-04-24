package com.infinity.omos.repository.user

import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
import com.infinity.omos.source.local.user.UserLocalDataSource
import com.infinity.omos.source.remote.user.UserRemoteDataSource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun loginUser(userCredential: UserCredential): Result<UserToken> {
        return userRemoteDataSource.loginUser(userCredential)
    }

    override suspend fun loginSnsUser(userSnsCredential: UserSnsCredential): Result<UserToken> {
        return userRemoteDataSource.loginSnsUser(userSnsCredential)
    }

    override suspend fun getUserIdFromEmail(email: String): Result<UserId> {
        return userRemoteDataSource.getUserIdFromEmail(email)
    }

    override suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult> {
        return userRemoteDataSource.changePassword(userPassword)
    }

    override suspend fun isNotEmailDuplicate(email: String): Result<NetworkResult> {
        return userRemoteDataSource.isNotEmailDuplicate(email)
    }

    override suspend fun signUpUser(userSignUp: UserSignUp): Result<NetworkResult> {
        return userRemoteDataSource.signUpUser(userSignUp)
    }

    override suspend fun saveToken(token: UserToken) {
        userLocalDataSource.saveToken(token)
    }
}