package com.infinity.omos.viewmodels

import androidx.lifecycle.ViewModel
import com.infinity.omos.data.user.profile.Profile
import com.infinity.omos.repository.FollowRepository
import com.infinity.omos.repository.RemoveUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject internal constructor(
    private val followRepository: FollowRepository,
    private val removeUserRepository: RemoveUserRepository
): ViewModel() {

    fun getFollower(userId: Int): Flow<List<Profile>> {
        return followRepository.getFollower(userId)
    }

    fun getFollowing(userId: Int): Flow<List<Profile>> {
        return followRepository.getFollowing(userId)
    }

    fun getUserProfile(userId: Int): Flow<Profile> {
        return removeUserRepository.getUserProfile(userId)
    }
}