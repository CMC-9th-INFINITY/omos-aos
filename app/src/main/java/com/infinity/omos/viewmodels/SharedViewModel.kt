package com.infinity.omos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.MyRecord
import com.infinity.omos.repository.Repository

/**
 *  수정 필요 ( Repository 인자 )
 */
class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    val myRecord = repository.getMyRecordData(1)
    var oneLineRecord = repository.getMyRecordData(2)
    var myOstRecord = repository.getMyRecordData(3)
    var myStoryRecord = repository.getMyRecordData(4)
    var interpretRecord = repository.getMyRecordData(-1)
    var freeRecord = repository.getMyRecordData(5)

    var isEmpty = MutableLiveData<Boolean>()

    init {
        isEmpty.value = false
    }

    // xml 연결 (listData)
    fun getMyRecordData(): LiveData<List<MyRecord>> {
        isEmpty.value = myRecord.value == null
        return myRecord
    }
}