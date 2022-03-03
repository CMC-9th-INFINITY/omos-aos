package com.infinity.omos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.Album
import com.infinity.omos.repository.Repository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()

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