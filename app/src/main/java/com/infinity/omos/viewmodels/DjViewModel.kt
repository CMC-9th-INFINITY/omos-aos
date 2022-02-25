package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.MyRecord
import com.infinity.omos.repository.Repository

class DjViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    val djRecord = repository.getMyRecordData(1)

    // xml 연결 (djRecordListData)
    fun getDjRecordData(): LiveData<List<MyRecord>> {
        return djRecord
    }
}