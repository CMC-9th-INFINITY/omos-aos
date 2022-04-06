package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.ResultState
import com.infinity.omos.repository.MyPageRepository
import com.infinity.omos.repository.OnBoardingRepository

class ChangePwViewModel(application: Application): AndroidViewModel(application) {
    private val repository = OnBoardingRepository()
    var visibleEye1 = MutableLiveData<Boolean>()
    var visibleEye2 = MutableLiveData<Boolean>()

    var stateInput = MutableLiveData<Boolean>()

    init {
        visibleEye1.value = false
        visibleEye2.value = false
    }

    fun eye1Click(){
        visibleEye1.value = visibleEye1.value != true
    }

    fun eye2Click(){
        visibleEye2.value = visibleEye2.value != true
    }

    fun checkInput(pw: EditText, pwAgain: EditText){
        stateInput.value = pw.length() > 0 && pwAgain.length() > 0
    }

    // 프로필 변경
    fun updatePassword(password: String, userId: Int){
        repository.updatePassword(password, userId)
    }
    fun getStateUpdatePw(): LiveData<ResultState>{
        return repository.stateUpdatePw
    }
}