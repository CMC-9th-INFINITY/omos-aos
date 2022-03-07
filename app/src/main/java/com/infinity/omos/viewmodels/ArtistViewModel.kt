package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.repository.SearchRepository

class ArtistViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    private val _album = repository._album
    private val _music = repository._music
    val album = _album
    val music = _music

    val stateMusic = repository._stateMusic
    val stateAlbum = repository._stateAlbum

    fun loadMoreAlbum(keyword: String, limit: Int, offset: Int){
        repository.getAlbum(keyword, limit, offset)
    }

    fun loadMoreMusic(keyword: String, limit: Int, offset: Int){
        repository.getMusic(keyword, limit, offset)
    }
}