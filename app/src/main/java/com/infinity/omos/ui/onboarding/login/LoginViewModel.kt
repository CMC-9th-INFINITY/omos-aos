package com.infinity.omos.ui.onboarding.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private var _isVisiblePassword = MutableStateFlow(false)
    val isVisiblePassword = _isVisiblePassword.asStateFlow()

    private var _isActivatedLogin = MutableStateFlow(false)
    val isActivatedLogin = _isActivatedLogin.asStateFlow()

    fun changePasswordVisibleState() {
        _isVisiblePassword.value = isVisiblePassword.value.not()
    }

    fun changeLoginState() {
        _isActivatedLogin.value = email.value.isNotEmpty() && password.value.isNotEmpty()
    }
}