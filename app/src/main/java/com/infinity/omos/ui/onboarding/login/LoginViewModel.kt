package com.infinity.omos.ui.onboarding.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private var _isVisiblePassword = MutableStateFlow(false)
    val isVisiblePassword = _isVisiblePassword.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun changePasswordVisibleState() {
        _isVisiblePassword.value = isVisiblePassword.value.not()
    }
}