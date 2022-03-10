package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.repository.SearchRepository

class MusicRecordViewModel(application: Application): AndroidViewModel(application)  {

    private val repository: SearchRepository = SearchRepository()
    private val _musicRecord = repository._musicRecord
    val musicRecord = _musicRecord
    val stateMusicRecord =repository._stateMusicRecord

    fun loadMoreRecord(musicId: String, postId: Int?, size: Int, userId: Int){
        repository.getMusicRecord(musicId, postId, size, userId)
    }
}