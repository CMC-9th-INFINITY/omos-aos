package com.infinity.omos.ui.record.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.record.DetailRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailRecordViewModel(
    savedStateHandle: SavedStateHandle,
    dataStoreManager: DataStoreManager,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val postId: Int = savedStateHandle.get<Int>(POST_ID_SAVED_STATE_KEY)!!
    private val userId = dataStoreManager.getUserId()

    private var _detailRecordUiState = MutableStateFlow<DetailRecordUiState>(DetailRecordUiState.Loading)
    val detailRecordUiState = _detailRecordUiState.asStateFlow()

    init {
        fetchDetailRecord()
    }

    private fun fetchDetailRecord() {
        viewModelScope.launch {
            recordRepository.getDetailRecord(postId, userId)
                .mapCatching { it.toPresentation() }
                .onSuccess { _detailRecordUiState.value = DetailRecordUiState.Success(it) }
                .onFailure { _detailRecordUiState.value = DetailRecordUiState.Error }
        }
    }

    companion object {
        private const val POST_ID_SAVED_STATE_KEY = "postId"
    }
}

sealed interface DetailRecordUiState {
    object Loading : DetailRecordUiState
    data class Success(
        val detailRecord: DetailRecordModel
    ) : DetailRecordUiState
    object Error : DetailRecordUiState
}