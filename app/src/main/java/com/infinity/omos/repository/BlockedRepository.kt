package com.infinity.omos.repository

import com.infinity.omos.api.BlockedService
import com.infinity.omos.api.FollowService
import com.infinity.omos.api.RetrofitAPI
import com.infinity.omos.data.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import javax.inject.Inject

class BlockedRepository @Inject constructor(private val service: BlockedService){

    fun getBlockedResult(userId: Int): Flow<List<Profile>> {
        return flow {
            val result = service.getBlockedUsers(userId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}