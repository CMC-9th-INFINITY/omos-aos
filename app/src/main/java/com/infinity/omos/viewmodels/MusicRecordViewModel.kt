package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.SearchRepository

class MusicRecordViewModel(application: Application): AndroidViewModel(application)  {

    private val repository: SearchRepository = SearchRepository()

    fun setMusicRecord(musicId: String, postId: Int?, size: Int, sortType: String, userId: Int){
        repository.getMusicRecord(musicId, postId, size, sortType, userId)
    }
    fun getMusicRecord():LiveData<List<Record>>{
        return repository.musicRecord
    }
    fun getStateMusicRecord(): LiveData<Constant.ApiState>{
        return repository.stateMusicRecord
    }
}