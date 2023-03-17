package com.infinity.omos.ui.onboarding.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {

    private val pattern: Pattern = Patterns.EMAIL_ADDRESS

    private var _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private var _errorEmail = MutableStateFlow(ErrorField(false))
    val errorEmail = _errorEmail.asStateFlow()

    private var _errorPassword = MutableStateFlow(ErrorField(false))
    val errorPassword = _errorPassword.asStateFlow()

    private var _isVisiblePassword = MutableStateFlow(false)
    val isVisiblePassword = _isVisiblePassword.asStateFlow()

    private var _isActivatedLogin = MutableStateFlow(false)
    val isActivatedLogin = _isActivatedLogin.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun changePasswordVisibleState() {
        _isVisiblePassword.value = isVisiblePassword.value.not()
    }

    fun changeLoginState() {
        _isActivatedLogin.value = email.value.isNotEmpty() && password.value.isNotEmpty()
    }

    fun checkEmailValidation(hasFocus: Boolean) {
        if (hasFocus.not()) {
            if (email.value.isNotEmpty()) {
                val state = pattern.matcher(email.value).matches().not()
                _errorEmail.value = ErrorField(
                    state,
                    if (state) "입력하신 내용을 다시 확인해주세요." else ""
                )
            } else {
                _errorEmail.value = ErrorField(
                    true,
                    "이메일을 입력해주세요."
                )
            }
        }
    }

    fun checkPasswordValidation(hasFocus: Boolean) {
        if (hasFocus.not()) {
            if (password.value.isEmpty()) {
                _errorPassword.value = ErrorField(
                    true,
                    "비밀번호를 입력해주세요."
                )
            } else {
                _errorPassword.value = ErrorField(
                    false
                )
            }
        }
    }
}