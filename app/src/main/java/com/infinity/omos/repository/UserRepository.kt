package com.infinity.omos.repository

import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserPassword
import com.infinity.omos.data.user.UserSnsCredential
import com.infinity.omos.data.user.UserToken
import com.infinity.omos.source.local.UserLocalDataSource
import com.infinity.omos.source.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun loginUser(userCredential: UserCredential): Result<UserToken> {
        return userRemoteDataSource.loginUser(userCredential)
    }

    suspend fun loginSnsUser(userSnsCredential: UserSnsCredential): Result<UserToken> {
        return userRemoteDataSource.loginSnsUser(userSnsCredential)
    }

    suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult> {
        return userRemoteDataSource.changePassword(userPassword)
    }

    suspend fun saveToken(token: UserToken) {
        userLocalDataSource.saveToken(token)
    }
}