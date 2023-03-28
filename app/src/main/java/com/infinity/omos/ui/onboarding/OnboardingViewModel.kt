package com.infinity.omos.ui.onboarding

import androidx.lifecycle.ViewModel
import com.infinity.omos.utils.Pattern
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class OnboardingViewModel : ViewModel() {

    protected var _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    protected var _authCode = MutableStateFlow("")
    val authCode = _authCode.asStateFlow()

    protected var correctAuthCode = ""

    protected var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    protected var _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    protected var _errorEmail = MutableStateFlow(ErrorField(false))
    val errorEmail = _errorEmail.asStateFlow()

    protected var _errorAuthCode = MutableStateFlow(ErrorField(false))
    val errorAuthCode = _errorAuthCode.asStateFlow()

    protected var _errorPassword = MutableStateFlow(ErrorField(false))
    val errorPassword = _errorPassword.asStateFlow()

    protected var _errorConfirmPassword = MutableStateFlow(ErrorField(false))
    val errorConfirmPassword = _errorConfirmPassword.asStateFlow()

    protected var _isVisiblePassword = MutableStateFlow(false)
    val isVisiblePassword = _isVisiblePassword.asStateFlow()

    protected var _isVisibleConfirmPassword = MutableStateFlow(false)
    val isVisibleConfirmPassword = _isVisibleConfirmPassword.asStateFlow()

    protected var _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    protected val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    protected var _authCodeState = MutableStateFlow<AuthCodeState>(AuthCodeState.Nothing)
    val authCodeState = _authCodeState.asStateFlow()

    protected var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    abstract fun changeCompleteState()

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

    fun setPassword(password: String) {
        if (errorPassword.value.state) {
            _errorPassword.value = ErrorField(false)
        }
        _password.value = password
    }

    fun setConfirmPassword(password: String) {
        if (errorConfirmPassword.value.state) {
            _errorConfirmPassword.value = ErrorField(false)
        }
        _confirmPassword.value = password
    }

    fun changePasswordVisibleState() {
        _isVisiblePassword.value = isVisiblePassword.value.not()
    }

    fun changeConfirmPasswordVisibleState() {
        _isVisibleConfirmPassword.value = isVisibleConfirmPassword.value.not()
    }

    fun isValidEmail(): Boolean {
        return if (email.value.isNotEmpty()) {
            val state = Pattern.emailPattern.matcher(email.value).matches().not()
            _errorEmail.value = ErrorField(
                state,
                if (state) OnboardingState.Failure.INCORRECT_CONTENTS_ERROR_MESSAGE else ""
            )
            state
        } else {
            val state = true
            _errorEmail.value = ErrorField(
                state,
                OnboardingState.Failure.BLANK_EMAIL_ERROR_MESSAGE
            )
            state
        }
    }

    fun isValidAuthCode(maxLength: Int): Boolean {
        if (authCode.value.length == maxLength && correctAuthCode == authCode.value) {
            // 인증 성공
            val isError = false
            _errorEmail.value = ErrorField(
                isError,
                OnboardingState.Failure.CORRECT_AUTH_CODE_MESSAGE
            )
            _authCodeState.value = AuthCodeState.Success
            return isError.not()
        }

        _errorAuthCode.value = if (authCode.value.length == maxLength && correctAuthCode != authCode.value) {
            // 인증 실패
            ErrorField(
                true,
                OnboardingState.Failure.INCORRECT_AUTH_CODE_ERROR_MESSAGE
            )
        } else {
            ErrorField(false, "")
        }

        return false
    }

    fun isValidPassword(hasFocus: Boolean): Boolean {
        if (hasFocus.not()) {
            return if (password.value.isEmpty()) {
                val isError = true
                _errorPassword.value = ErrorField(
                    isError,
                    OnboardingState.Failure.BLANK_PASSWORD_ERROR_MESSAGE
                )
                isError.not()
            } else if (Pattern.passwordPattern.matches(password.value).not()) {
                val isError = true
                _errorPassword.value = ErrorField(
                    isError,
                    OnboardingState.Failure.NOT_MATCH_PASSWORD_ERROR_MESSAGE
                )
                isError.not()
            } else {
                val isError = false
                _errorPassword.value = ErrorField(isError)
                isError.not()
            }
        }
        return false
    }

    fun isValidConfirmPassword(hasFocus: Boolean): Boolean {
        if (hasFocus.not()) {
            return if (confirmPassword.value.isEmpty()) {
                val isError = true
                _errorConfirmPassword.value = ErrorField(
                    isError,
                    OnboardingState.Failure.BLANK_PASSWORD_ERROR_MESSAGE
                )
                isError.not()
            } else if (password.value != confirmPassword.value) {
                val isError = true
                _errorConfirmPassword.value = ErrorField(
                    isError,
                    OnboardingState.Failure.NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE
                )
                isError.not()
            } else {
                val isError = false
                _errorConfirmPassword.value = ErrorField(isError)
                isError.not()
            }
        }

        return false
    }

    sealed class Event {
        object ShowDialog : Event()
    }
}