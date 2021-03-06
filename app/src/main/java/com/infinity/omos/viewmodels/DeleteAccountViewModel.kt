package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.MyPageRepository

class DeleteAccountViewModel(application: Application): AndroidViewModel(application) {

    private val repository = MyPageRepository()

    // 계정탈퇴
    fun signOut(userId: Int){
        repository.signOut(userId)
    }
    fun getStateSignOut(): LiveData<Constant.ApiState> {
        return repository.stateSignOut
    }
}