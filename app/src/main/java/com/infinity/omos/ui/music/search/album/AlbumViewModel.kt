package com.infinity.omos.ui.music.search.album

import androidx.lifecycle.ViewModel
import com.infinity.omos.repository.music.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): ViewModel() {

    // 앨범 ID, 앨범 이미지, 타이틀, 가수 이름, 날짜 가져오기

    // 음악 리스트 가져오기
}