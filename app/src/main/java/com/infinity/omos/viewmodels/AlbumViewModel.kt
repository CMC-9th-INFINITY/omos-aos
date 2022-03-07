package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.repository.SearchRepository

class AlbumViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    private val _albumDetail = repository._albumDetail
    val albumDetail = _albumDetail
    val stateAlbumDetail = repository._stateAlbumDetail

    fun loadMoreMusic(albumId: String){
        repository.getAlbumDetail(albumId)
    }
}