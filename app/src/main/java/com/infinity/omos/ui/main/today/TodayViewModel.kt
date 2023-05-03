package com.infinity.omos.ui.main.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.repository.today.TodayRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val todayRepository: TodayRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    fun fetchFamousRecord() {
        viewModelScope.launch {
            todayRepository.getFamousRecord()
                .onSuccess {

                }
                .onFailure {

                }
        }
    }

    fun fetchLovedMusic() {
        viewModelScope.launch {
            todayRepository.getLovedMusic(userId)
                .onSuccess {

                }
                .onFailure {

                }
        }
    }

    fun fetchTodayMusic() {
        viewModelScope.launch {
            todayRepository.getTodayMusic()
                .onSuccess {

                }
                .onFailure {

                }
        }
    }

    fun fetchRecommendedDj() {
        viewModelScope.launch {
            todayRepository.getRecommendedDj()
                .onSuccess {

                }
                .onFailure {

                }
        }
    }
}