package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.MyRecord
import com.infinity.omos.repository.Repository

/**
 *  수정 필요 ( Repository 인자 )
 */
class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    private val myRecord = repository.getMyRecordData(1)
    var oneLine = repository.getMyRecordData(2)
    var myOst = repository.getMyRecordData(3)

    // xml 연결 (listData)
    fun getMyRecordData(): LiveData<List<MyRecord>> {
        return myRecord
    }
}