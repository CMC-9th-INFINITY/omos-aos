package com.infinity.omos.ui.music.search.artist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.infinity.omos.data.music.album.toPresentation
import com.infinity.omos.repository.music.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    musicRepository: MusicRepository
) : ViewModel() {

    val artistId: String = savedStateHandle.get<String>(ARTIST_ID_SAVED_STATE_KEY)!!
    val artistName: String = savedStateHandle.get<String>(ARTIST_NAME_SAVED_STATE_KEY)!!

    val albumStream = musicRepository.getArtistAlbumStream(artistId)
        .map { pagingData ->
            pagingData.map { album ->
                album.toPresentation()
            }
        }
        .cachedIn(viewModelScope)

    companion object {
        private const val ARTIST_ID_SAVED_STATE_KEY = "artistId"
        private const val ARTIST_NAME_SAVED_STATE_KEY = "artistName"
    }
}