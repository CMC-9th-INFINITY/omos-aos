package com.infinity.omos.source.remote.follow

import com.infinity.omos.data.user.Profile

interface FollowRemoteDataSource {

    suspend fun getMyDjs(userId: Int) : Result<List<Profile>>
}