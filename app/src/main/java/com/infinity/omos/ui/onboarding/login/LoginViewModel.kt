package com.infinity.omos.ui.onboarding.login

import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserCredential
import com.infinity.omos.data.user.UserSnsCredential
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.INCORRECT_CONTENTS_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.NOT_EXIST_USER_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : OnboardingViewModel() {

    override fun changeCompleteState() {
        _isCompleted.value =
            validateEmailWithError().state.not() && validatePasswordWithError().state.not()
    }

    override fun validatePasswordWithError(): ErrorField {
        return if (password.value.isEmpty()) {
            ErrorField(true, OnboardingState.Failure.BLANK_PASSWORD_ERROR_MESSAGE)
        } else {
            ErrorField(false)
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