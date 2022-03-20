package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.ResultState
import com.infinity.omos.repository.MyPageRepository

class SettingViewModel(application: Application): AndroidViewModel(application) {

    private val repository = MyPageRepository()

    fun doLogout(userId: Int){
        repository.doLogout(userId)
    }
    fun getStateLogout(): LiveData<ResultState>{
        return repository.stateLogout
    }
}