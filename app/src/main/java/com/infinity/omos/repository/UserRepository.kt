package com.infinity.omos.repository

import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserToken
import com.infinity.omos.source.remote.UserRemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun loginUser(userCredential: UserCredential): Result<UserToken> {
        return userRemoteDataSource.loginUser(userCredential)
    }
}