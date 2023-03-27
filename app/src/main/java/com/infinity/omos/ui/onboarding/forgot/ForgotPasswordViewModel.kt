package com.infinity.omos.ui.onboarding.forgot

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserEmail
import com.infinity.omos.repository.AuthRepository
import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.OnboardingState
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.BLANK_EMAIL_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.CORRECT_AUTH_CODE_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.INCORRECT_AUTH_CODE_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.INCORRECT_CONTENTS_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.NETWORK_ERROR_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val pattern: Pattern = PatternsCompat.EMAIL_ADDRESS

    private var _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private var _authCode = MutableStateFlow("")
    val authCode = _authCode.asStateFlow()

    private var correctAuthCode = ""

    private var _errorEmail = MutableStateFlow(ErrorField(false))
    val errorEmail = _errorEmail.asStateFlow()

    private var _errorAuthCode = MutableStateFlow(ErrorField(false))
    val errorAuthCode = _errorAuthCode.asStateFlow()

    private var _isActivatedNext = MutableStateFlow(false)
    val isActivatedNext = _isActivatedNext.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    fun setEmail(email: String) {
        if (errorEmail.value.state) {
            _errorEmail.value = ErrorField(false)
        }
        _email.value = email
    }

    fun setAuthCode(code: String) {
        if (errorAuthCode.value.state) {
            _errorAuthCode.value = ErrorField(false)
        }
        _authCode.value = code
    }

    fun changeNextState() {
        _isActivatedNext.value = state.value is OnboardingState.Success
    }

    fun checkEmailValidation() {
        _errorEmail.value = if (email.value.isNotEmpty()) {
            val state = pattern.matcher(email.value).matches().not()
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

    fun checkAuthCodeValidation(maxLength: Int) {
        if (authCode.value.length == maxLength && correctAuthCode == authCode.value) {
            // 인증 성공
            _isActivatedNext.value = true
            _errorEmail.value = ErrorField(
                false,
                CORRECT_AUTH_CODE_MESSAGE
            )
        }

        _errorAuthCode.value = if (authCode.value.length == maxLength && correctAuthCode != authCode.value) {
            // 인증 실패
            ErrorField(
                true,
                INCORRECT_AUTH_CODE_ERROR_MESSAGE
            )
        } else {
            ErrorField(false, "")
        }
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
                    _state.value = OnboardingState.Success
                }
                .onFailure { _state.value = OnboardingState.Failure(NETWORK_ERROR_MESSAGE) }
        }
    }

    sealed class Event {
        object ShowDialog : Event()
    }
}