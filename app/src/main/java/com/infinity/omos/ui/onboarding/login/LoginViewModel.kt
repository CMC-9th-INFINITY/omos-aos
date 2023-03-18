package com.infinity.omos.ui.onboarding.login

import android.util.Log
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val pattern: Pattern = PatternsCompat.EMAIL_ADDRESS

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

    fun loginUser() {
        viewModelScope.launch {
            userRepository.loginUser(
                UserCredential(
                    email.value,
                    password.value
                )
            )
                .onSuccess { userToken ->
                    // TODO: 토큰 저장 및 화면 이동 이벤트 발생
                    Log.d("jaemin", userToken.userId.toString())
                }
                .onFailure {
                    // TODO: 에러 메시지 띄우기
                    Log.d("jaemin", "로그인 에러")
                }
        }
    }
}