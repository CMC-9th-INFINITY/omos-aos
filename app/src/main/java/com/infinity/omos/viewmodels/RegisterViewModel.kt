package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.repository.OnBoardingRepository
import com.infinity.omos.repository.Repository

class RegisterViewModel(application: Application): AndroidViewModel(application)  {

    private val repository: OnBoardingRepository = OnBoardingRepository()

    var stateInput = MutableLiveData<Boolean>()
    var stateDupEmail = repository._stateDupEmail

    var visibleEye1 = MutableLiveData<Boolean>()
    var visibleEye2 = MutableLiveData<Boolean>()

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

    fun checkInput(id: EditText, pw: EditText, pwAgain: EditText){
        stateInput.value = id.length() > 0 && pw.length() > 0 && pwAgain.length() > 0
    }

    fun checkDupEmail(email: String){
        repository.checkDupEmail(email)
    }
}