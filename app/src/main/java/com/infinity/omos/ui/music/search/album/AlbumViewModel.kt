package com.infinity.omos.ui.music.search.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.infinity.omos.repository.music.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository
): ViewModel() {

    val albumId: String = savedStateHandle.get<String>(ALBUM_ID_SAVED_STATE_KEY)!!
    val albumTitle: String = savedStateHandle.get<String>(ALBUM_TITLE_ID_SAVED_STATE_KEY)!!
    val albumImageUrl: String = savedStateHandle.get<String>(ALBUM_IMAGE_URL_ID_SAVED_STATE_KEY)!!
    val artists: String = savedStateHandle.get<String>(ARTISTS_ID_SAVED_STATE_KEY)!!
    val releaseDate: String = savedStateHandle.get<String>(RELEASE_DATE_ID_SAVED_STATE_KEY)!!

    // 음악 리스트 가져오기

    companion object {
        private const val ALBUM_ID_SAVED_STATE_KEY = "albumId"
        private const val ALBUM_TITLE_ID_SAVED_STATE_KEY = "albumTitle"
        private const val ALBUM_IMAGE_URL_ID_SAVED_STATE_KEY = "albumImageUrl"
        private const val ARTISTS_ID_SAVED_STATE_KEY = "artists"
        private const val RELEASE_DATE_ID_SAVED_STATE_KEY = "releaseDate"
    }
}