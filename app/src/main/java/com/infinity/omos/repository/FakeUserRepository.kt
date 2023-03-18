package com.infinity.omos.repository

import com.infinity.omos.api.FakeUserService
import com.infinity.omos.data.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FakeUserRepository @Inject internal constructor(private val service: FakeUserService){

    fun getUserProfile(userId: Int): Flow<Profile> {
        return flow {
            val result = service.getUserProfile(userId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}