package com.infinity.omos.viewmodels

import android.app.Application
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.UserLogin
import com.infinity.omos.data.UserSNSLogin
import com.infinity.omos.data.UserToken
import com.infinity.omos.repository.Repository

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    var visibleEye = MutableLiveData<Boolean>()
    var stateInput = MutableLiveData<Boolean>()

    var statusLogin = repository._status
    var stateSns = repository._stateSns

    init {
        visibleEye.value = false
        stateInput.value = false
    }

    fun eyeClick(){
        visibleEye.value = visibleEye.value != true
    }

    fun checkInput(id: EditText, pw: EditText){
        stateInput.value = id.length() > 0 && pw.length() > 0
    }

    fun checkLogin(userLogin: UserLogin){
        repository.checkLogin(userLogin)
    }

    fun checkSnsLogin(userSNSLogin: UserSNSLogin){
        repository.checkSnsLogin(userSNSLogin)
    }
}