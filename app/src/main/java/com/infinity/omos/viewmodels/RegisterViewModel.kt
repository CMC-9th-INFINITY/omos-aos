package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.OnBoardingRepository

class RegisterViewModel(application: Application): AndroidViewModel(application)  {

    private val repository: OnBoardingRepository = OnBoardingRepository()

    var stateInput = MutableLiveData<Boolean>()

    var visibleEye1 = MutableLiveData<Boolean>()
    var visibleEye2 = MutableLiveData<Boolean>()
    var visibleEye3 = MutableLiveData<Boolean>()

    init {
        visibleEye1.value = false
        visibleEye2.value = false
        visibleEye3.value = false
    }

    fun eye1Click(){
        visibleEye1.value = visibleEye1.value != true
    }

    fun eye2Click(){
        visibleEye2.value = visibleEye2.value != true
    }

    fun eye3Click(){
        visibleEye3.value = visibleEye3.value != true
    }

    fun checkInput(id: EditText, pw: EditText, pwAgain: EditText){
        stateInput.value = id.length() > 0 && pw.length() > 0 && pwAgain.length() > 0
    }

    fun checkDupEmail(email: String){
        repository.checkDupEmail(email)
    }
    fun getStateDupEmail(): LiveData<Constant.ApiState>{
        return repository.stateDupEmail
    }

    // 이메일 인증 코드
    fun sendEmailAuth(email: String){
        repository.getEmailCode(email)
    }
    fun getCode(): LiveData<String>{
        return repository.emailCode
    }
    fun getStateGetCode(): LiveData<Constant.ApiState>{
        return repository.stateGetCode
    }
}