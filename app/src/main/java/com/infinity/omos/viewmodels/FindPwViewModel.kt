package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.FakeUserId
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.OnBoardingRepository

class FindPwViewModel(application: Application): AndroidViewModel(application) {

    private val repository: OnBoardingRepository = OnBoardingRepository()
    var visibleEye = MutableLiveData<Boolean>()
    var stateInput = MutableLiveData<Boolean>()

    init {
        visibleEye.value = false
    }

    fun eyeClick(){
        visibleEye.value = visibleEye.value != true
    }

    fun checkInput(id: EditText, code: EditText){
        stateInput.value = id.length() > 0 && code.length() > 0
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
    fun getCode(): LiveData<String> {
        return repository.emailCode
    }
    fun getStateGetCode(): LiveData<Constant.ApiState> {
        return repository.stateGetCode
    }

    // userId 가져오기
    fun setUserIdFromEmail(email: String){
        repository.getUserIdFromEmail(email)
    }
    fun getUserIdFromEmail(): LiveData<FakeUserId>{
        return repository.getFakeUserId
    }
}