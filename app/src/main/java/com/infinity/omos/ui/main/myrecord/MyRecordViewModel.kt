package com.infinity.omos.ui.main.myrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.record.VerticalRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecordViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMyRecords()
    }

    private fun fetchMyRecords() {
        viewModelScope.launch {
            recordRepository.getMyRecords(userId).mapCatching { records ->
                records.map { it.toPresentation() }
            }
                .onSuccess { records ->
                    _uiState.value = UiState.Success(records)
                }
                .onFailure { _uiState.value = UiState.Error }
        }
    }
}

sealed interface UiState {
    object Loading : UiState
    data class Success(val records: List<VerticalRecordModel>) : UiState
    object Error : UiState
}