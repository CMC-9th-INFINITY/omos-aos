package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.UserLogin
import com.infinity.omos.data.UserSnsLogin
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.OnBoardingRepository

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val repository: OnBoardingRepository = OnBoardingRepository()
    var visibleEye = MutableLiveData<Boolean>()
    var stateInput = MutableLiveData<Boolean>()

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
    fun getStateLogin(): LiveData<Constant.ApiState>{
        return repository.stateLogin
    }

    fun checkSnsLogin(userSnsLogin: UserSnsLogin){
        repository.checkSnsLogin(userSnsLogin)
    }
    fun getStateSnsLogin(): LiveData<Constant.ApiState>{
        return repository.stateSnsLogin
    }
}