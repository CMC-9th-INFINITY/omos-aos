package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.data.UserSignUp
import com.infinity.omos.data.UserSnsSignUp
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.OnBoardingRepository

class RegisterNickViewModel(application: Application): AndroidViewModel(application) {

    private val repository: OnBoardingRepository = OnBoardingRepository()

    var checkBoxTos = MutableLiveData<Boolean>()
    var checkBoxPP = MutableLiveData<Boolean>()
    var allState = MutableLiveData<Boolean>()

    init {
        checkBoxTos.value = false
        checkBoxPP.value = false
        allState.value = false
    }

    fun checkBoxTosClick(){
        checkBoxTos.value = checkBoxTos.value != true
        checkState()
    }

    fun checkBoxPPClick(){
        checkBoxPP.value = checkBoxPP.value != true
        checkState()
    }

    private fun checkState(){
        allState.value = checkBoxTos.value == true && checkBoxPP.value == true
    }

    fun signUp(userInfo: UserSignUp){
        repository.signUp(userInfo)
    }
    fun getStateSignUp(): LiveData<Constant.ApiState> {
        return repository.stateSignUp
    }

    fun snsSignUp(userInfo: UserSnsSignUp){
        repository.snsSignUp(userInfo)
    }
    fun getStateSnsSignUp(): LiveData<Constant.ApiState> {
        return repository.stateSnsSignUp
    }
}