package com.infinity.omos.repository.follow

import com.infinity.omos.data.user.Profile
import com.infinity.omos.source.remote.follow.FollowRemoteDataSource
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val followRemoteDataSource: FollowRemoteDataSource
) : FollowRepository {

    override suspend fun getMyDjs(userId: Int): Result<List<Profile>> {
        return followRemoteDataSource.getMyDjs(userId)
    }
}