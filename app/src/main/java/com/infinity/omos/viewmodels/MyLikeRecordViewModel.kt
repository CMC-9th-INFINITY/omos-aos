package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.SimpleRecord
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.MyPageRepository

class MyLikeRecordViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MyPageRepository()

    fun setLikeRecord(userId: Int){
        repository.getLikeRecord(userId)
    }
    fun getLikeRecord(): LiveData<List<SimpleRecord>> {
        return repository.likeRecord
    }
    fun getStateLikeRecord(): LiveData<Constant.ApiState> {
        return repository.stateLikeRecord
    }
}