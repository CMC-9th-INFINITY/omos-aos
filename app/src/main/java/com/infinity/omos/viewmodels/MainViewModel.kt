package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.Album
import com.infinity.omos.data.Artists
import com.infinity.omos.data.Music
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.SearchRepository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository()

    // 노래 검색
    private var music = repository.music
    fun loadMoreMusic(keyword: String, limit: Int, offset: Int){
        repository.getMusic(keyword, limit, offset)
    }
    fun getMusic(): LiveData<List<Music>> {
        return music
    }
    fun getStateMusic(): LiveData<Constant.ApiState> {
        return repository.stateMusic
    }

    // 앨범 검색
    private var album = repository.album
    fun loadMoreAlbum(keyword: String, limit: Int, offset: Int){
        repository.getAlbum(keyword, limit, offset)
    }
    fun getAlbum(): LiveData<List<Album>>{
        return album
    }
    fun getStateAlbum(): LiveData<Constant.ApiState>{
        return repository.stateAlbum
    }

    // 가수 검색
    private var artist = repository.artist
    fun loadMoreArtist(keyword: String, limit: Int, offset: Int){
        repository.getArtist(keyword, limit, offset)
    }
    fun getArtist(): LiveData<List<Artists>>{
        return artist
    }
    fun getStateArtist(): LiveData<Constant.ApiState>{
        return repository.stateArtist
    }
}