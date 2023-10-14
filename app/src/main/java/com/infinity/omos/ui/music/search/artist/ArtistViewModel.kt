package com.infinity.omos.ui.music.search.artist

import androidx.lifecycle.ViewModel
import com.infinity.omos.repository.music.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    // 가수 ID, 이름 가져오기

    // 앨범 리스트 가져오기
}