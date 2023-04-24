package com.infinity.omos.ui.onboarding.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserEmail
import com.infinity.omos.repository.auth.AuthRepository
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.AuthCodeState
import com.infinity.omos.ui.onboarding.base.Event
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.utils.Pattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private var _authCode = MutableStateFlow("")
    val authCode = _authCode.asStateFlow()

    private var correctAuthCode = ""

    private var _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private var _authCodeState = MutableStateFlow<AuthCodeState>(AuthCodeState.Nothing)
    val authCodeState = _authCodeState.asStateFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    fun changeCompleteState(state: Boolean) {
        _isCompleted.value = state
    }

    fun setEmail(text: String) {
        _email.value = text
    }

    fun getEmailErrorMessage(text: String): ErrorMessage {
        return if (text.isNotEmpty()) {
            val state = Pattern.emailPattern.matcher(text).matches().not()
            if (state) {
                ErrorMessage.INCORRECT_CONTENTS_ERROR_MESSAGE
            } else {
                ErrorMessage.NO_ERROR
            }
        } else {
            ErrorMessage.BLANK_EMAIL_ERROR_MESSAGE
        }
    }

    fun setAuthCode(text: String) {
        _authCode.value = text
    }

    fun getAuthCodeErrorMessage(text: String, maxLength: Int): ErrorMessage {
        return if (text.length == maxLength && correctAuthCode != text) {
            // 인증 실패
            ErrorMessage.INCORRECT_AUTH_CODE_ERROR_MESSAGE
        } else {
            ErrorMessage.NO_ERROR
        }
    }

    fun setAuthCodeState() {
        _authCodeState.value = AuthCodeState.Success
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
                .onFailure { _state.value = OnboardingState.Failure(ErrorMessage.NETWORK_ERROR_MESSAGE) }
        }
    }
}