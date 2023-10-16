package com.infinity.omos.ui.main.allrecords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.record.CategoryModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllRecordsViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchAllRecords()
    }

    fun refresh() {
        fetchAllRecords()
    }

    private fun fetchAllRecords() {
        viewModelScope.launch {
            recordRepository.getAllRecords(userId).mapCatching { categories ->
                categories.toPresentation()
            }
                .onSuccess { categories ->
                    _uiState.value = UiState.Success(categories)
                }
                .onFailure { _uiState.value = UiState.Error }
        }
    }
}

sealed interface UiState {
    object Loading : UiState
    data class Success(val categories: List<CategoryModel>) : UiState
    object Error : UiState
}