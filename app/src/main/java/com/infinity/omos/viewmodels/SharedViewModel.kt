package com.infinity.omos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.MyDj
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

    var myDjRecord = repository._djRecord
    var myDj = repository.getMyDjData(6)

    var isEmpty = MutableLiveData<Boolean>()

    init {
        repository.getMyDjRecordData(1)
        isEmpty.value = false
    }

    // xml 연결 (myRecordListData)
    fun getMyRecordData(): LiveData<List<MyRecord>> {
        isEmpty.value = myRecord.value == null
        return myRecord
    }

    // xml 연결 (myDjListData)
    fun getMyDjData(): LiveData<List<MyDj>> {
        return myDj
    }

    fun updateDjRecord(pos: Int){
        repository.getMyDjRecordData(pos)
    }
}