package com.infinity.omos.repository.dj

import com.infinity.omos.data.user.profile.DjProfile
import com.infinity.omos.data.user.profile.Profile

interface DjRepository {

    suspend fun getMyDjs(userId: Int): Result<List<Profile>>

    suspend fun getDjProfile(fromUserId: Int, toUserId: Int): Result<DjProfile>
}