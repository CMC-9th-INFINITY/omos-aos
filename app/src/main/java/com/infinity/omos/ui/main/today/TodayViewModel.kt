package com.infinity.omos.ui.main.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.music.LovedMusicModel
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.data.record.HorizontalPreviewRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.data.user.profile.ProfileModel
import com.infinity.omos.data.user.toPresentation
import com.infinity.omos.repository.today.TodayRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    private fun fetchTodayMusic(): Job {
        return viewModelScope.launch {
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

    private fun fetchFamousRecords(): Job {
        return viewModelScope.launch {
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

    private fun fetchRecommendedDjs(): Job {
        return viewModelScope.launch {
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

    private fun fetchLovedMusic(): Job {
        return viewModelScope.launch {
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

    fun refresh() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val job1 = fetchTodayMusic()
            val job2 = fetchFamousRecords()
            val job3 = fetchRecommendedDjs()
            val job4 = fetchLovedMusic()

            joinAll(job1, job2, job3, job4)
            _uiState.value = UiState.Complete
        }
    }
}

sealed interface TodayMusicUiState {
    data class Success(val todayMusic: MusicModel) : TodayMusicUiState
    object Error : TodayMusicUiState
    object Loading : TodayMusicUiState
}

sealed interface FamousRecordsUiState {
    data class Success(val famousRecords: List<HorizontalPreviewRecordModel>) : FamousRecordsUiState
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

sealed interface UiState {
    object Complete : UiState
    object Loading : UiState
}