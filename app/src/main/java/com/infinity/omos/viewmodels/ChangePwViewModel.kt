package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.ResultState
import com.infinity.omos.repository.MyPageRepository

class ChangePwViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MyPageRepository()
    var visibleEye = MutableLiveData<Boolean>()

    init {
        visibleEye.value = false
    }

    fun eyeClick(){
        visibleEye.value = visibleEye.value != true
    }

    // 프로필 변경
    fun updatePassword(password: String, userId: Int){
        repository.updatePassword(password, userId)
    }
    fun getStateUpdatePw(): LiveData<ResultState>{
        return repository.stateUpdatePw
    }
}