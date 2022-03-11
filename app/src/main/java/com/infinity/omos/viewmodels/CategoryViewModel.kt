package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.AllRecordsRepository
import com.infinity.omos.repository.RecordRepository

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val allRecordRepository: AllRecordsRepository = AllRecordsRepository()
    private val recordRepository: RecordRepository = RecordRepository()

    fun setCategoryRecord(category: String, postId: Int?, size: Int, sortType: String, userId: Int){
        allRecordRepository.getCategory(category, postId, size, sortType, userId)
    }
    fun getCategoryRecord(): LiveData<List<Record>>{
        return allRecordRepository._category
    }
    fun getStateCategory(): LiveData<Constant.ApiState>{
        return allRecordRepository.stateCategory
    }

    fun saveLike(postId: Int, userId: Int){
        recordRepository.saveLike(postId, userId)
    }

    fun deleteLike(postId: Int, userId: Int){
        recordRepository.deleteLike(postId, userId)
    }

    fun saveScrap(postId: Int, userId: Int){
        recordRepository.saveScrap(postId, userId)
    }

    fun deleteScrap(postId: Int, userId: Int){
        recordRepository.deleteLike(postId, userId)
    }
}