package com.infinity.omos.ui.main.mydj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.infinity.omos.data.record.DetailRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.data.user.ProfileModel
import com.infinity.omos.data.user.toPresentation
import com.infinity.omos.repository.follow.FollowRepository
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyDjViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    recordRepository: RecordRepository,
    private val followRepository: FollowRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    val detailRecords: Flow<PagingData<DetailRecordModel>> =
        recordRepository.getDetailRecordsStream().map { records ->
            records.map { it.toPresentation() }
        }.cachedIn(viewModelScope)

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
                    _uiState.value = UiState.Error
                }
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