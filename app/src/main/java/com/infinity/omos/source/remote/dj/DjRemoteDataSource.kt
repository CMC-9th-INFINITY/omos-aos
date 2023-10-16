package com.infinity.omos.source.remote.dj

import com.infinity.omos.data.user.profile.DjProfile
import com.infinity.omos.data.user.profile.Profile

interface DjRemoteDataSource {

    suspend fun getMyDjs(userId: Int) : Result<List<Profile>>

    suspend fun getDjProfile(fromUserId: Int, toUserId: Int): Result<DjProfile>
}