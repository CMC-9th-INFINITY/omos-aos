package com.infinity.omos.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.MyRecord
import com.infinity.omos.repository.AllRecordRepository
import com.infinity.omos.repository.MyDjRepository
import com.infinity.omos.repository.Repository

/**
 *  수정 필요 ( Repository 인자 )
 */
class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    private val allRecordRepository: AllRecordRepository = AllRecordRepository()
    private val myDjRepository: MyDjRepository = MyDjRepository()

    // My 레코드
    private val _myRecord = repository._myRecord
    val myRecord = _myRecord
    val stateMyRecord = repository._stateMyRecord

    // 전체 레코드
    private val _allRecords = allRecordRepository._allRecords
    val allRecords = _allRecords
    val stateAllRecords = allRecordRepository._stateAllRecords

    // My DJ
    var myDjRecord = myDjRepository._djRecord
    var myDj = myDjRepository.getMyDjData(6)

    init {
        myDjRepository.getMyDjRecordData(1)
    }

    // xml 연결 (myRecordListData)
    fun getMyRecordData(): LiveData<List<MyRecord>> {
        return myRecord
    }

    fun loadMoreMyRecord(page: Int){
        repository.getMyRecordData(page)
    }

    // xml 연결 (myDjListData)
    fun getMyDjData(): LiveData<List<MyDj>> {
        return myDj
    }

    fun setAllRecords(){
        allRecordRepository.setAllRecords()
    }

    fun updateDjRecord(pos: Int){
        myDjRepository.getMyDjRecordData(pos)
    }
}