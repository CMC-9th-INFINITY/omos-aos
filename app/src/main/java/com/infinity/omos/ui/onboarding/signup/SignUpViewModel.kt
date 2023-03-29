package com.infinity.omos.ui.onboarding.signup

import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserEmail
import com.infinity.omos.repository.AuthRepository
import com.infinity.omos.ui.onboarding.base.AuthCodeState
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : OnboardingViewModel() {

    override fun changeCompleteState() {
        _isCompleted.value =
            authCodeState.value is AuthCodeState.Success && validatePasswordWithError().state.not() && password.value == confirmPassword.value
    }

    fun sendAuthMail() {
        _authCode.value = ""
        _state.value = OnboardingState.Loading
        viewModelScope.launch {
            val email = UserEmail(email.value)
            authRepository.sendAuthMail(email)
                .onSuccess { authCode ->
                    correctAuthCode = authCode.code
                    _event.emit(Event.ShowDialog)
                    _authCodeState.value = AuthCodeState.Ready
                    _state.value = OnboardingState.Success
                }
                .onFailure {
                    _state.value =
                        OnboardingState.Failure(OnboardingState.Failure.NETWORK_ERROR_MESSAGE)
                }
        }
    }
}