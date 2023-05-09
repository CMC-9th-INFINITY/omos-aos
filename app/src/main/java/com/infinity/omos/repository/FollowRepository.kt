package com.infinity.omos.repository

import com.infinity.omos.api.FollowService
import com.infinity.omos.data.user.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FollowRepository @Inject constructor(private val service: FollowService) {

    fun getFollower(userId: Int): Flow<List<Profile>> {
        return flow {
            val result = service.getFollower(userId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    fun getFollowing(userId: Int): Flow<List<Profile>> {
        return flow<List<Profile>> {
            val result = service.getFollowing(userId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}