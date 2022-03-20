package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.data.ResultState
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.MyPageRepository

class ChangeProfileViewModel(application: Application): AndroidViewModel(application) {

    private val repository = MyPageRepository()

    fun updateProfile(nickName: String, profileUrl: String, userId: Int){
        repository.updateProfile(nickName, profileUrl, userId)
    }
    fun getStateUpdateProfile(): LiveData<Constant.ApiState>{
        return repository.stateUpdateProfile
    }
}