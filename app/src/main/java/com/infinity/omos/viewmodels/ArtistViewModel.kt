package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.repository.SearchRepository

class ArtistViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    private val _artistMusic = repository._artistMusic
    private val _artistAlbum = repository._artistAlbum

    val artistMusic = _artistMusic
    val artistAlbum = _artistAlbum

    val stateArtistMusic = repository._stateArtistMusic
    val stateArtistAlbum = repository._stateArtistAlbum

    fun loadMoreAlbum(keyword: String, limit: Int, offset: Int){
        repository.getArtistAlbum(keyword, limit, offset)
    }

    fun getArtistMusic(artistId: String){
        repository.getArtistMusic(artistId)
    }
}