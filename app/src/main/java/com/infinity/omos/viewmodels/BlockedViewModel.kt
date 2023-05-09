package com.infinity.omos.viewmodels

import androidx.lifecycle.ViewModel
import com.infinity.omos.data.user.Profile
import com.infinity.omos.data.ResultState
import com.infinity.omos.repository.BlockedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BlockedViewModel @Inject internal constructor(
    private val repository: BlockedRepository
) : ViewModel() {

    fun getBlockedUsers(userId: Int): Flow<List<Profile>> {
        return repository.getBlockedResult(userId)
    }

    fun cancelBlock(fromUserId: Int, toUserId: Int): Flow<ResultState> {
        return repository.cancelBlock(fromUserId, toUserId)
    }
}