package com.infinity.omos.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.infinity.omos.data.Profile
import com.infinity.omos.repository.FollowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject internal constructor(
    private val repository: FollowRepository
): ViewModel() {

    fun getFollower(userId: Int): Flow<List<Profile>> {
        return repository.getFollower(userId)
    }

    fun getFollowing(userId: Int): Flow<List<Profile>> {
        return repository.getFollowing(userId)
    }
}