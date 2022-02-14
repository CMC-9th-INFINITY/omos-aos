package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class RegisterNickViewModel(application: Application): AndroidViewModel(application) {
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
}