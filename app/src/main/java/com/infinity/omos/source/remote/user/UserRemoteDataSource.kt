package com.infinity.omos.source.remote.user

import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.*
import com.infinity.omos.data.user.login.*

interface UserRemoteDataSource {

    suspend fun loginUser(userCredential: UserCredential): Result<UserToken>

    suspend fun loginSnsUser(userSnsCredential: UserSnsCredential): Result<UserToken>

    suspend fun getUserIdFromEmail(email: String): Result<UserId>

    suspend fun changePassword(userPassword: UserPassword): Result<NetworkResult>

    suspend fun isNotEmailDuplicate(email: String): Result<NetworkResult>

    suspend fun signUpUser(userSignUp: UserSignUp): Result<NetworkResult>
}