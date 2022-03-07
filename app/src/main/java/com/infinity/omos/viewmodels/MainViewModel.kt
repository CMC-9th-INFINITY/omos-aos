package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.repository.SearchRepository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    private val _album = repository._album
    private val _music = repository._music
    private val _artist = repository._artist
    val album = _album
    val music = _music
    val artist = _artist

    val stateMusic = repository._stateMusic
    val stateAlbum = repository._stateAlbum
    val stateArtist = repository._stateArtist

    fun loadMoreAlbum(keyword: String, limit: Int, offset: Int){
        repository.getAlbum(keyword, limit, offset)
    }

    fun loadMoreMusic(keyword: String, limit: Int, offset: Int){
        repository.getMusic(keyword, limit, offset)
    }

    fun loadMoreArtist(keyword: String, limit: Int, offset: Int){
        repository.getArtist(keyword, limit, offset)
    }
}