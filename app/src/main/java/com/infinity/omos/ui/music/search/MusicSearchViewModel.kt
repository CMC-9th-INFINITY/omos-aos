package com.infinity.omos.ui.music.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.infinity.omos.data.music.album.AlbumModel
import com.infinity.omos.data.music.artist.ArtistModel
import com.infinity.omos.data.music.MusicModel
import com.infinity.omos.data.music.MusicTitleModel
import com.infinity.omos.data.music.toPresentation
import com.infinity.omos.repository.music.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MusicSearchViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    var keyword = MutableStateFlow("")

    private val searchedFlow = MutableSharedFlow<String>()

    private var _musicTitlesUiState = MutableStateFlow<MusicTitleUiState>(MusicTitleUiState.Loading)
    val musicTitlesUiState = _musicTitlesUiState.asStateFlow()

    private var _searchState = MutableStateFlow(MusicSearchState.BEFORE)
    val searchState = _searchState.asStateFlow()

    lateinit var musicStream: Flow<PagingData<MusicModel>>

    lateinit var albumStream: Flow<PagingData<AlbumModel>>

    lateinit var artistStream: Flow<PagingData<ArtistModel>>

    init {
        fetchSearchMusic()
    }

    private fun fetchSearchMusic() {
        viewModelScope.launch {
            searchedFlow
                .debounce(100) // 적절한 시간 간격 (밀리초 단위)을 설정합니다.
                .distinctUntilChanged()
                .mapLatest { keyword ->
                    musicRepository.getMusicTitle(keyword)
                }
                .collect { result ->
                    result.mapCatching { titles ->
                        titles.map { title ->
                            title.toPresentation()
                        }
                    }
                        .onSuccess { titles ->
                            _musicTitlesUiState.value = MusicTitleUiState.Success(titles)
                        }
                        .onFailure {
                            _musicTitlesUiState.value = MusicTitleUiState.Error
                        }
                }
        }
    }

    fun setKeyword(text: String) {
        keyword.value = text
        viewModelScope.launch {
            searchedFlow.emit(text)
        }
        setSearchState(MusicSearchState.ING)
    }

    fun setSearchState(state: MusicSearchState) {
        _searchState.value = state
    }

    fun search() {
        setSearchState(MusicSearchState.AFTER)
        musicStream = musicRepository.getMusicStream(keyword.value)
            .map { pagingData ->
                pagingData.map { music ->
                    music.toPresentation()
                }
            }
            .cachedIn(viewModelScope)

        albumStream = musicRepository.getAlbumStream(keyword.value)
            .map { pagingData ->
                pagingData.map { album ->
                    album.toPresentation()
                }
            }
            .cachedIn(viewModelScope)

        artistStream = musicRepository.getArtistStream(keyword.value)
            .map { pagingData ->
                pagingData.map { artist ->
                    artist.toPresentation()
                }
            }
            .cachedIn(viewModelScope)
    }
}

sealed interface MusicTitleUiState {
    object Loading : MusicTitleUiState
    data class Success(val titles: List<MusicTitleModel>) : MusicTitleUiState
    object Error : MusicTitleUiState
}