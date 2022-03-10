package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.Album
import com.infinity.omos.data.ArtistMusic
import com.infinity.omos.data.Music
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.SearchRepository

class ArtistViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    fun setArtistAlbum(keyword: String, limit: Int, offset: Int){
        repository.getArtistAlbum(keyword, limit, offset)
    }
    fun getArtistAlbum(): LiveData<List<Album>> {
        return repository.artistAlbum
    }
    fun getStateArtistAlbum(): LiveData<Constant.ApiState> {
        return repository.stateArtistAlbum
    }

    fun setArtistMusic(artistId: String){
        repository.getArtistMusic(artistId)
    }
    fun getArtistMusic(): LiveData<List<ArtistMusic>> {
        return repository.artistMusic
    }
    fun getStateArtistMusic(): LiveData<Constant.ApiState> {
        return repository.stateArtistMusic
    }
}