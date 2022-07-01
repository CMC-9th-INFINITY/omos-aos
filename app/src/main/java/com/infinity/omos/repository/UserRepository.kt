package com.infinity.omos.repository

import com.infinity.omos.api.UserService
import com.infinity.omos.data.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import javax.inject.Inject

class UserRepository @Inject internal constructor(private val service: UserService){

    fun getUserProfile(userId: Int): Flow<Profile> {
        return flow {
            val result = service.getUserProfile(userId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}