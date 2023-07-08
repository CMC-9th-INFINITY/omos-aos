package com.infinity.omos.ui.music

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchMusicViewModel @Inject constructor(

) : ViewModel() {

    var keyword = ""

    private var _searching = MutableStateFlow(false)
    val searching = _searching.asStateFlow()

    fun changeMusicKeywordVisibility() {
        _searching.value = keyword.isNotEmpty()
    }

    fun fetchSearchMusic() {

    }
}