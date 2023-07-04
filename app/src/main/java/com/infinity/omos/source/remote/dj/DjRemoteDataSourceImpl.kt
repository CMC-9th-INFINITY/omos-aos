package com.infinity.omos.source.remote.dj

import com.infinity.omos.api.DjService
import com.infinity.omos.data.user.profile.DjProfile
import com.infinity.omos.data.user.profile.Profile
import javax.inject.Inject

class DjRemoteDataSourceImpl @Inject constructor(
    private val djService: DjService
) : DjRemoteDataSource {

    override suspend fun getMyDjs(userId: Int): Result<List<Profile>> {
        return Result.runCatching {
            djService.getMyDjs(userId)
        }
    }

    override suspend fun getDjProfile(fromUserId: Int, toUserId: Int): Result<DjProfile> {
        return Result.runCatching {
            djService.getDjProfile(fromUserId, toUserId)
        }
    }
}