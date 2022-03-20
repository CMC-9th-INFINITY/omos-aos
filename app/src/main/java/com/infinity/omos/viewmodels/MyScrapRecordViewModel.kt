package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.SimpleRecord
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.MyPageRepository

class MyScrapRecordViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MyPageRepository()

    fun setScrapRecord(userId: Int){
        repository.getScrapRecord(userId)
    }
    fun getScrapRecord(): LiveData<List<SimpleRecord>>{
        return repository.scrapRecord
    }
    fun getStateScrapRecord(): LiveData<Constant.ApiState>{
        return repository.stateScrapRecord
    }
}