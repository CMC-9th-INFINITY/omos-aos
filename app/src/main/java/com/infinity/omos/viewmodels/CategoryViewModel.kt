package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.Record
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.AllRecordsRepository

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val repository: AllRecordsRepository = AllRecordsRepository()

    fun setCategoryRecord(category: String, postId: Int?, size: Int, sortType: String, userId: Int){
        repository.getCategory(category, postId, size, sortType, userId)
    }
    fun getCategoryRecord(): LiveData<List<Record>>{
        return repository._category
    }
    fun getStateCategory(): LiveData<Constant.ApiState>{
        return repository.stateCategory
    }
}