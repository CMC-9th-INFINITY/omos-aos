package com.infinity.omos.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.infinity.omos.data.Profile
import com.infinity.omos.repository.FollowRepository
import com.infinity.omos.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject internal constructor(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun getFollower(userId: Int): Flow<List<Profile>> {
        return followRepository.getFollower(userId)
    }

    fun getFollowing(userId: Int): Flow<List<Profile>> {
        return followRepository.getFollowing(userId)
    }

    fun getUserProfile(userId: Int): Flow<Profile> {
        return userRepository.getUserProfile(userId)
    }
}