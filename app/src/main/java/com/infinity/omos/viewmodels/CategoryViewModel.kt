package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.infinity.omos.repository.AllRecordRepository

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val repository: AllRecordRepository = AllRecordRepository()
    private val _category = repository._category
    val category = _category
    val stateCategory =repository._stateCategory

    fun loadMoreCategory(category: String, page: Int, size: Int, sort: String?, userId: Int){
        repository.getCategory(category, page, size, sort, userId)
    }
}