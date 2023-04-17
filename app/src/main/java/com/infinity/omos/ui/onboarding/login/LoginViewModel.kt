package com.infinity.omos.ui.onboarding.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserSnsCredential
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.INCORRECT_CONTENTS_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.NOT_EXIST_USER_ERROR_MESSAGE
import com.infinity.omos.utils.Pattern
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

    private var _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    fun changeCompleteState(state: Boolean) {
        _isCompleted.value = state
    }

    fun validateEmail(text: String): ErrorMessage {
        _email.value = text
        return if (email.value.isNotEmpty()) {
            val state = Pattern.emailPattern.matcher(email.value).matches().not()
            if (state) {
                ErrorMessage.INCORRECT_CONTENTS_ERROR_MESSAGE
            } else {
                ErrorMessage.NO_ERROR
            }
        } else {
            ErrorMessage.BLANK_EMAIL_ERROR_MESSAGE
        }
    }

    fun validatePassword(text: String): ErrorMessage {
        _password.value = text
        return if (password.value.isEmpty()) {
            ErrorMessage.BLANK_PASSWORD_ERROR_MESSAGE
        } else {
            ErrorMessage.NO_ERROR
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