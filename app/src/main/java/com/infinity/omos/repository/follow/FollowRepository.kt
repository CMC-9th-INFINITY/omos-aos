package com.infinity.omos.repository.follow

import com.infinity.omos.data.user.Profile

interface FollowRepository {

    suspend fun getMyDjs(userId: Int) : Result<List<Profile>>
}