package com.infinity.omos.source.remote.follow

import com.infinity.omos.api.FollowService
import com.infinity.omos.data.user.Profile
import javax.inject.Inject

class FollowRemoteDataSourceImpl @Inject constructor(
    private val followService: FollowService
) : FollowRemoteDataSource {

    override suspend fun getMyDjs(userId: Int): Result<List<Profile>> {
        return Result.runCatching {
            followService.getMyDjs(userId)
        }
    }
}