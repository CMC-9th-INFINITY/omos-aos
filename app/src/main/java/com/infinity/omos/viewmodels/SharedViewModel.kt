package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.MyRecord
import com.infinity.omos.repository.Repository

/**
 *  수정 필요 ( Repository 인자 )
 */
class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    private val records = repository.getMyRecordData()

    // xml 연결 (listData)
    fun getMyRecordData(): LiveData<List<MyRecord>> {
        return records
    }
}