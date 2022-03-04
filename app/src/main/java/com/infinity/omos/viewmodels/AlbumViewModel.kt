package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.repository.Repository

class AlbumViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()

    private val _albumDetail = repository._albumDetail
    val albumDetail = _albumDetail
    val stateAlbumDetail = repository._stateAlbumDetail

    fun loadMoreMusic(albumId: String){
        repository.getAlbumDetail(albumId)
    }
}