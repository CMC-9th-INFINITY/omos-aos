package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.MyDj
import com.infinity.omos.data.Profile
import com.infinity.omos.repository.AllRecordRepository
import com.infinity.omos.repository.MyDjRepository
import com.infinity.omos.repository.MyRecordRepository
import com.infinity.omos.utils.GlobalApplication

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
    private val userId = GlobalApplication.prefs.getLong("userId").toInt()
    private val myDj = myDjRepository.getMyDj(userId)

    // xml 연결 (myDjListData)
    fun getMyDjData(): LiveData<List<Profile>?> {
        return myDj
    }

    fun getMyRecord(userId: Int){
        myRecordRepository.getMyRecord(userId)
    }

    fun setAllRecords(){
        allRecordRepository.setAllRecords()
    }
}