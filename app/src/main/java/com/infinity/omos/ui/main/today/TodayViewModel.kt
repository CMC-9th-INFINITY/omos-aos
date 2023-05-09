package com.infinity.omos.ui.main.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.music.LovedMusicModel
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.data.record.SumRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.data.user.ProfileModel
import com.infinity.omos.data.user.toPresentation
import com.infinity.omos.repository.today.TodayRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val todayRepository: TodayRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private val _todayMusicUiState = MutableStateFlow<TodayMusicUiState>(TodayMusicUiState.Loading)
    val todayMusicUiState = _todayMusicUiState.asStateFlow()

    private val _famousRecordsUiState = MutableStateFlow<FamousRecordsUiState>(FamousRecordsUiState.Loading)
    val famousRecordsUiState = _famousRecordsUiState.asStateFlow()

    private val _recommendedDjsUiState = MutableStateFlow<RecommendedDjsUiState>(RecommendedDjsUiState.Loading)
    val recommendedDjsUiState = _recommendedDjsUiState.asStateFlow()

    private val _lovedMusicUiState = MutableStateFlow<LovedMusicUiState>(LovedMusicUiState.Loading)
    val lovedMusicUiState = _lovedMusicUiState.asStateFlow()

    init {
        fetchTodayMusic()
        fetchFamousRecords()
        fetchRecommendedDjs()
        fetchLovedMusic()
    }

    private fun fetchTodayMusic() {
        viewModelScope.launch {
            todayRepository.getTodayMusic()
                .mapCatching { it.toPresentation() }
                .onSuccess { music ->
                    _todayMusicUiState.value = TodayMusicUiState.Success(music)
                }
                .onFailure {
                    _todayMusicUiState.value = TodayMusicUiState.Error
                }
        }
    }

    private fun fetchFamousRecords() {
        viewModelScope.launch {
            todayRepository.getFamousRecords()
                .mapCatching { records -> records.map { it.toPresentation() } }
                .onSuccess { records ->
                    _famousRecordsUiState.value = FamousRecordsUiState.Success(records)
                }
                .onFailure {
                    _famousRecordsUiState.value = FamousRecordsUiState.Error
                }
        }
    }

    private fun fetchRecommendedDjs() {
        viewModelScope.launch {
            todayRepository.getRecommendedDjs()
                .mapCatching { djs -> djs.map { it.toPresentation() } }
                .onSuccess { djs ->
                    _recommendedDjsUiState.value = RecommendedDjsUiState.Success(djs)
                }
                .onFailure {
                    _recommendedDjsUiState.value = RecommendedDjsUiState.Error
                }
        }
    }

    private fun fetchLovedMusic() {
        viewModelScope.launch {
            todayRepository.getLovedMusic(userId)
                .mapCatching { it.toPresentation() }
                .onSuccess { music ->
                    _lovedMusicUiState.value = LovedMusicUiState.Success(music)
                }
                .onFailure {
                    _lovedMusicUiState.value = LovedMusicUiState.Error
                }
        }
    }
}

sealed interface TodayMusicUiState {
    data class Success(val todayMusic: MusicModel) : TodayMusicUiState
    object Error : TodayMusicUiState
    object Loading : TodayMusicUiState
}

sealed interface FamousRecordsUiState {
    data class Success(val famousRecords: List<SumRecordModel>) : FamousRecordsUiState
    object Error : FamousRecordsUiState
    object Loading : FamousRecordsUiState
}

sealed interface RecommendedDjsUiState {
    data class Success(val recommendedDjs: List<ProfileModel>) : RecommendedDjsUiState
    object Error : RecommendedDjsUiState
    object Loading : RecommendedDjsUiState
}

sealed interface LovedMusicUiState {
    data class Success(val lovedMusic: LovedMusicModel) : LovedMusicUiState
    object Error : LovedMusicUiState
    object Loading : LovedMusicUiState
}