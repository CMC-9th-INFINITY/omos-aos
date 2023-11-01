package com.infinity.omos.ui.main.mydj

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.infinity.omos.data.record.DetailRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.data.user.profile.ProfileModel
import com.infinity.omos.data.user.toPresentation
import com.infinity.omos.repository.dj.DjRepository
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyDjViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val recordRepository: RecordRepository,
    private val djRepository: DjRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private var toUserId = -1

    val detailRecords: MutableStateFlow<PagingData<DetailRecordModel>> =
        MutableStateFlow(PagingData.empty())

    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMyDjs()
        fetchDetailRecords()
    }

    fun refresh() {
        fetchDetailRecords()
    }

    fun changeToUserId(isSelected: Boolean, userId: Int) {
        toUserId = if (isSelected) {
            userId
        } else {
            -1
        }
    }

    private fun fetchMyDjs() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            djRepository.getMyDjs(userId).mapCatching { djs ->
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

    /**
     * toUserId 가 -1 인 경우는 모든 DJ 레코드들을 불러온다.
     */
    fun fetchDetailRecords() {
        recordRepository.getDetailRecordsStream(toUserId).map { records ->
            records.map { it.toPresentation(userId) }
        }
            .onEach { pagingData ->
                detailRecords.value = pagingData
            }
            .cachedIn(viewModelScope)
            .launchIn(viewModelScope)
    }
}

sealed interface UiState {
    object Loading : UiState
    data class Success(val djs: List<ProfileModel>) : UiState
    object Error : UiState
}