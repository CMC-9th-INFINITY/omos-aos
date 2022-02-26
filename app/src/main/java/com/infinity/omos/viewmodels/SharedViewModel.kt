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
    var oneLineRecord = repository.getTestData(2)
    var myOstRecord = repository.getTestData(3)
    var myStoryRecord = repository.getTestData(4)
    var interpretRecord = repository.getTestData(-1)
    var freeRecord = repository.getTestData(5)

    // MyRecord
    val myRecord = repository.getMyRecordData(1)
    val stateMyRecord = repository._stateMyRecord

    // MyDJ
    var myDjRecord = repository._djRecord
    var myDj = repository.getMyDjData(6)

    init {
        repository.getMyDjRecordData(1)
    }

    // xml 연결 (myRecordListData)
    fun getMyRecordData(): LiveData<List<MyRecord>> {
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