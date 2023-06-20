package com.infinity.omos.ui.main.mydj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.ProfileModel
import com.infinity.omos.data.user.toPresentation
import com.infinity.omos.repository.follow.FollowRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyDjViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val followRepository: FollowRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMyDjs()
    }

    fun refresh() {
        fetchMyDjs()
    }

    private fun fetchMyDjs() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            followRepository.getMyDjs(userId).mapCatching { djs ->
                djs.map { it.toPresentation() }
            }
                .onSuccess { djs ->
                    _uiState.value = UiState.Success(djs)
                }
                .onFailure {
                    _uiState.value = UiState.Error }
        }
    }

    fun fetchDetailRecords() {

    }
}

sealed interface UiState {
    object Loading : UiState
    data class Success(val djs: List<ProfileModel>) : UiState
    object Error : UiState
}