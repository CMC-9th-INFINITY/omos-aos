package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.record.DetailRecord
import com.infinity.omos.data.ResultState
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.AllRecordsRepository
import com.infinity.omos.repository.FakeRecordRepository
import com.infinity.omos.repository.ReportBlockRepository

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val allRecordRepository: AllRecordsRepository = AllRecordsRepository()
    private val fakeRecordRepository: FakeRecordRepository = FakeRecordRepository()
    private val reportRepository: ReportBlockRepository = ReportBlockRepository()

    fun setCategoryRecord(category: String, postId: Int?, size: Int, sortType: String, userId: Int){
        allRecordRepository.getCategory(category, postId, size, sortType, userId)
    }
    fun getCategoryRecord(): LiveData<List<DetailRecord>>{
        return allRecordRepository._category
    }
    fun getStateCategory(): LiveData<Constant.ApiState>{
        return allRecordRepository.stateCategory
    }

    fun saveLike(postId: Int, userId: Int){
        fakeRecordRepository.saveLike(postId, userId)
    }

    fun deleteLike(postId: Int, userId: Int){
        fakeRecordRepository.deleteLike(postId, userId)
    }

    fun saveScrap(postId: Int, userId: Int){
        fakeRecordRepository.saveScrap(postId, userId)
    }

    fun deleteScrap(postId: Int, userId: Int){
        fakeRecordRepository.deleteScrap(postId, userId)
    }

    fun reportObject(fromUserId: Int, recordId: Int?, reportReason: String?, toUserId: Int?, type: String){
        reportRepository.reportObject(fromUserId, recordId, reportReason, toUserId, type)
    }
    fun getStateReportRecord(): LiveData<ResultState>{
        return reportRepository.stateReportObject
    }
}