package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.MyDj
import com.infinity.omos.repository.AllRecordRepository
import com.infinity.omos.repository.MyDjRepository
import com.infinity.omos.repository.MyRecordRepository

/**
 *  수정 필요 ( Repository 인자 )
 */
class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val myRecordRepository: MyRecordRepository = MyRecordRepository()
    private val allRecordRepository: AllRecordRepository = AllRecordRepository()
    private val myDjRepository: MyDjRepository = MyDjRepository()

    // My 레코드
    private val _myRecord = myRecordRepository._myRecord
    val myRecord = _myRecord
    val stateMyRecord = myRecordRepository._stateMyRecord

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

//    // xml 연결 (myRecordListData)
//    fun getMyRecordData(): LiveData<List<MyRecord>> {
//        return myRecord
//    }

    // xml 연결 (myDjListData)
    fun getMyDjData(): LiveData<List<MyDj>> {
        return myDj
    }

    fun getMyRecord(userId: Int){
        myRecordRepository.getMyRecord(userId)
    }

    fun setAllRecords(){
        allRecordRepository.setAllRecords()
    }

    fun updateDjRecord(pos: Int){
        myDjRepository.getMyDjRecordData(pos)
    }
}