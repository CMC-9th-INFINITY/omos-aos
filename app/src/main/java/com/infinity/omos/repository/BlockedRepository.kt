package com.infinity.omos.repository

import com.infinity.omos.api.BlockService
import com.infinity.omos.data.Profile
import com.infinity.omos.data.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BlockedRepository @Inject constructor(private val service: BlockService){

    fun getBlockedResult(userId: Int): Flow<List<Profile>> {
        return flow {
            val result = service.getBlockedUsers(userId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun cancelBlock(fromUserId: Int, toUserId: Int): Flow<ResultState> {
        return flow {
            val result = service.cancelBlock(fromUserId, toUserId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}