package com.infinity.omos.repository.dj

import com.infinity.omos.data.user.profile.DjProfile
import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.source.remote.dj.DjRemoteDataSource
import javax.inject.Inject

class DjRepositoryImpl @Inject constructor(
    private val djRemoteDataSource: DjRemoteDataSource
) : DjRepository {

    override suspend fun getMyDjs(userId: Int): Result<List<Profile>> {
        return djRemoteDataSource.getMyDjs(userId)
    }

    override suspend fun getDjProfile(fromUserId: Int, toUserId: Int): Result<DjProfile> {
        return djRemoteDataSource.getDjProfile(fromUserId, toUserId)
    }
}