package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.Album
import com.infinity.omos.data.ArtistMusic
import com.infinity.omos.data.Music
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.SearchRepository

class AlbumViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    fun setAlbumDetail(albumId: String){
        repository.getAlbumDetail(albumId)
    }
    fun getAlbumDetail(): LiveData<List<Music>> {
        return repository.albumDetail
    }
    fun getStateAlbumDetail(): LiveData<Constant.ApiState> {
        return repository.stateAlbumDetail
    }
}