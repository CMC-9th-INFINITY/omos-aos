package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.RecordRepository

/**
 *  삭제 필요
 */

class MyRecordDetailViewModel(application: Application): AndroidViewModel(application) {

    private val repository: RecordRepository = RecordRepository()

    // 상세 레코드
    fun setDetailRecord(postId: Int, userId: Int){
        repository.getDetailRecord(postId, userId)
    }
    fun getDetailRecord(): LiveData<Record> {
        return repository.detailRecord
    }
    fun getStateDetailRecord(): LiveData<Constant.ApiState> {
        return repository.stateDetailRecord
    }
}