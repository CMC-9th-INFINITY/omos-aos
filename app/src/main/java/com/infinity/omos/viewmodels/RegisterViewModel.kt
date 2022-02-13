package com.infinity.omos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class RegisterViewModel(application: Application): AndroidViewModel(application)  {
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
}