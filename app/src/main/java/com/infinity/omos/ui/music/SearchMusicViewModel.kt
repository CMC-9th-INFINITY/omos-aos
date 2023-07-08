package com.infinity.omos.ui.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.music.MusicTitle
import com.infinity.omos.data.music.MusicTitleModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.repository.music.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchMusicViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private var keyword = ""

    private val searchedFlow = MutableSharedFlow<String>()

    private var _musicTitles = MutableStateFlow<MusicTitleUiState>(MusicTitleUiState.Loading)
    val musicTitles = _musicTitles.asStateFlow()

    init {
        fetchSearchMusic()
    }

    private fun fetchSearchMusic() {
        viewModelScope.launch {
            searchedFlow
                .debounce(300) // 적절한 시간 간격 (밀리초 단위)을 설정합니다.
                .distinctUntilChanged()
                .mapLatest { keyword ->
                    musicRepository.getMusicTitle(keyword)
                }
                .collect { result ->
                    result.mapCatching { titles ->
                        titles.map { title ->
                            title.toPresentation(keyword)
                        }
                    }
                        .onSuccess { titles ->
                            _musicTitles.value = MusicTitleUiState.Success(titles)
                        }
                        .onFailure {
                            _musicTitles.value = MusicTitleUiState.Error
                        }
                }
        }
    }

    fun setKeyword(text: String) {
        keyword = text
        viewModelScope.launch {
            searchedFlow.emit(text)
        }
    }
}

sealed interface MusicTitleUiState {
    object Loading : MusicTitleUiState
    data class Success(val titles: List<MusicTitleModel>) : MusicTitleUiState
    object Error : MusicTitleUiState
}