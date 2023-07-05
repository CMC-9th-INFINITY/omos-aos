package com.infinity.omos.ui.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.record.MyPageRecordModel
import com.infinity.omos.data.record.toPresentation
import com.infinity.omos.data.user.profile.DjProfileModel
import com.infinity.omos.data.user.toPresentation
import com.infinity.omos.repository.dj.DjRepository
import com.infinity.omos.repository.record.RecordRepository
import com.infinity.omos.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
    private val djRepository: DjRepository,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val userId = dataStoreManager.getUserId()

    private var _profile = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profile = _profile.asStateFlow()

    private var _myPageRecords = MutableStateFlow<MyPageRecordUiState>(MyPageRecordUiState.Loading)
    val myPageRecords = _myPageRecords.asStateFlow()

    init {
        fetchMyProfile()
        fetchMyPageRecords()
    }

    private fun fetchMyProfile() {
        viewModelScope.launch {
            djRepository.getDjProfile(userId, userId).mapCatching { it.toPresentation() }
                .onSuccess { profile ->
                    _profile.value = ProfileUiState.Success(profile)
                }
                .onFailure {
                    _profile.value = ProfileUiState.Error
                }
        }
    }

    private fun fetchMyPageRecords() {
        viewModelScope.launch {
            recordRepository.getMyPageRecords(userId).mapCatching { it.toPresentation() }
                .onSuccess { records ->
                    _myPageRecords.value = MyPageRecordUiState.Success(records)
                }
                .onFailure {
                    _myPageRecords.value = MyPageRecordUiState.Error
                }
        }
    }
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val profile: DjProfileModel) : ProfileUiState
    object Error : ProfileUiState
}

sealed interface MyPageRecordUiState {

    object Loading : MyPageRecordUiState
    data class Success(val myPageRecords: MyPageRecordModel) : MyPageRecordUiState
    object Error : MyPageRecordUiState
}