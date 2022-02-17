package com.infinity.omos.viewmodels

import android.app.Application
import android.content.res.ColorStateList
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.R
import com.infinity.omos.repository.Repository
import kotlinx.android.synthetic.main.activity_register_nick.*

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
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

    fun checkLogin(id: String, pw: String): Boolean{
        return repository.checkLogin(id, pw)
    }
}