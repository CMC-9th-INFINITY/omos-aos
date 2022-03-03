package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.repository.Repository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()

    private val _album = repository._album
    val album = _album
    val stateAlbum = repository._stateAlbum

    var keyword = MutableLiveData<String>()

    fun loadMoreAlbum(keyword: String, limit: Int, offset: Int){
        repository.getAlbum(keyword, limit, offset)
    }
}