package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.ResultState
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.MyRecordRepository
import com.infinity.omos.repository.FakeRecordRepository
import com.infinity.omos.repository.ReportBlockRepository

class DetailRecordViewModel(application: Application): AndroidViewModel(application) {

    private val repository: FakeRecordRepository = FakeRecordRepository()
    private val myRecordRepository = MyRecordRepository()
    private val reportRepository: ReportBlockRepository = ReportBlockRepository()

    // 상세 레코드
    fun setDetailRecord(postId: Int, userId: Int){
        repository.getDetailRecord(postId, userId)
    }
    fun getDetailRecord(): LiveData<DetailRecord>{
        return repository.detailRecord
    }
    fun getStateDetailRecord(): LiveData<Constant.ApiState>{
        return repository.stateDetailRecord
    }

    fun saveLike(postId: Int, userId: Int){
        repository.saveLike(postId, userId)
    }

    fun deleteLike(postId: Int, userId: Int){
        repository.deleteLike(postId, userId)
    }

    fun saveScrap(postId: Int, userId: Int){
        repository.saveScrap(postId, userId)
    }

    fun deleteScrap(postId: Int, userId: Int){
        repository.deleteScrap(postId, userId)
    }

    fun deleteRecord(postId: Int){
        myRecordRepository.deleteRecord(postId)
    }
    fun getStateDeleteRecord(): LiveData<ResultState>{
        return myRecordRepository.stateDeleteRecord
    }

    fun reportObject(fromUserId: Int, recordId: Int, reportReason: String?, toUserId: Int?, type: String){
        reportRepository.reportObject(fromUserId, recordId, reportReason, toUserId, type)
    }
    fun getStateReportRecord(): LiveData<ResultState>{
        return reportRepository.stateReportObject
    }
}