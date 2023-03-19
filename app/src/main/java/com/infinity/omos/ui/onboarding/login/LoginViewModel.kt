package com.infinity.omos.ui.onboarding.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private var _state = MutableStateFlow<LoginState>(LoginState.Nothing)
    val state = _state.asStateFlow()

    fun setEmail(email: String) {
        if (errorEmail.value.state) {
            _errorEmail.value = ErrorField(false)
        }
        _email.value = email
    }

    fun setPassword(password: String) {
        if (errorPassword.value.state) {
            _errorPassword.value = ErrorField(false)
        }
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
                    if (state) INCORRECT_CONTENTS_ERROR_MESSAGE else ""
                )
            } else {
                _errorEmail.value = ErrorField(
                    true,
                    BLANK_EMAIL_ERROR_MESSAGE
                )
            }
        }
    }

    fun checkPasswordValidation(hasFocus: Boolean) {
        if (hasFocus.not()) {
            if (password.value.isEmpty()) {
                _errorPassword.value = ErrorField(
                    true,
                    BLANK_PASSWORD_ERROR_MESSAGE
                )
            } else {
                _errorPassword.value = ErrorField(false)
            }
        }
    }

    fun loginUser() {
        _state.value = LoginState.Loading
        viewModelScope.launch {
            userRepository.loginUser(
                UserCredential(
                    email.value,
                    password.value
                )
            )
                .onSuccess { userToken ->
                    userRepository.saveToken(userToken)
                    _state.value = LoginState.Success
                }
                .onFailure {
                    _errorEmail.value = ErrorField(
                        true,
                        INCORRECT_CONTENTS_ERROR_MESSAGE
                    )
                    _errorPassword.value = ErrorField(
                        true,
                        INCORRECT_CONTENTS_ERROR_MESSAGE
                    )
                    _state.value = LoginState.Failure
                }
        }
    }

    companion object {
        const val INCORRECT_CONTENTS_ERROR_MESSAGE = "입력하신 내용을 다시 확인해주세요."
        const val BLANK_EMAIL_ERROR_MESSAGE = "이메일을 입력해주세요."
        const val BLANK_PASSWORD_ERROR_MESSAGE = "비밀번호를 입력해주세요."
    }
}