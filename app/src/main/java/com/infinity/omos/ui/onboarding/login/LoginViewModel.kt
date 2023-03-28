package com.infinity.omos.ui.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserSnsCredential
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.OnboardingState
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.BLANK_EMAIL_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.BLANK_PASSWORD_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.INCORRECT_CONTENTS_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.NOT_EXIST_USER_ERROR_MESSAGE
import com.infinity.omos.utils.Pattern.Companion.emailPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

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

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
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
            _errorEmail.value = if (email.value.isNotEmpty()) {
                val state = emailPattern.matcher(email.value).matches().not()
                ErrorField(
                    state,
                    if (state) INCORRECT_CONTENTS_ERROR_MESSAGE else ""
                )
            } else {
                ErrorField(
                    true,
                    BLANK_EMAIL_ERROR_MESSAGE
                )
            }
        }
    }

    fun checkPasswordValidation(hasFocus: Boolean) {
        if (hasFocus.not()) {
            _errorPassword.value = if (password.value.isEmpty()) {
                ErrorField(
                    true,
                    BLANK_PASSWORD_ERROR_MESSAGE
                )
            } else {
                ErrorField(false)
            }
        }
    }

    fun loginUser() {
        _state.value = OnboardingState.Loading

        // TODO: 이메일은 맞고 비밀번호는 틀릴 시 401 에러 문제 해결 후 타임아웃 없애기
        viewModelScope.launch {
            withTimeoutOrNull(5000) {
                userRepository.loginUser(
                    UserCredential(
                        email.value,
                        password.value
                    )
                )
                    .onSuccess { userToken ->
                        userRepository.saveToken(userToken)
                        _state.value = OnboardingState.Success
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
                        _state.value = OnboardingState.Failure(INCORRECT_CONTENTS_ERROR_MESSAGE)
                    }
            } ?: run {
                _state.value = OnboardingState.Failure(INCORRECT_CONTENTS_ERROR_MESSAGE)
            }
        }
    }

    fun loginKakaoUser(email: String) {
        _state.value = OnboardingState.Loading
        viewModelScope.launch {
            userRepository.loginSnsUser(
                UserSnsCredential(
                    email,
                    TYPE_KAKAO
                )
            )
                .onSuccess { userToken ->
                    userRepository.saveToken(userToken)
                    _state.value = OnboardingState.Success
                }
                .onFailure {
                    _state.value = OnboardingState.Failure(NOT_EXIST_USER_ERROR_MESSAGE)
                }
        }
    }

    companion object {
        const val TYPE_KAKAO = "KAKAO"
    }
}