package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class LoginViewModel(application: Application): AndroidViewModel(application) {
    var visibleEye = MutableLiveData<Boolean>()

    init {
        visibleEye.value = false
    }

    fun eyeClick(){
        visibleEye.value = visibleEye.value != true
    }
}