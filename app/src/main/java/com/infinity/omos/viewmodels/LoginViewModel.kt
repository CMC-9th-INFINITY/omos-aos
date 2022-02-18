package com.infinity.omos.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.infinity.omos.repository.Repository

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()
    var visibleEye = MutableLiveData<Boolean>()
    var stateInput = MutableLiveData<Boolean>()

    lateinit var accessToken: String
    var userId: Long = 0L

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

    fun addKakaoUser(token: String, id: Long?){
        accessToken = token
        userId = id!!
    }
}