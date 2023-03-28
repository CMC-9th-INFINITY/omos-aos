package com.infinity.omos.ui.onboarding.base

import androidx.lifecycle.ViewModel
import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.BLANK_EMAIL_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.BLANK_PASSWORD_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.CORRECT_AUTH_CODE_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.INCORRECT_AUTH_CODE_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.INCORRECT_CONTENTS_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.NOT_MATCH_PASSWORD_ERROR_MESSAGE
import com.infinity.omos.utils.Pattern
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

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

    fun checkEmailValidation(hasFocus: Boolean = false) {
        if (hasFocus) return

        _errorEmail.value = validateEmailWithError()
    }

    fun validateEmailWithError(): ErrorField {
        return if (email.value.isNotEmpty()) {
            val state = Pattern.emailPattern.matcher(email.value).matches().not()
            ErrorField(state, if (state) INCORRECT_CONTENTS_ERROR_MESSAGE else "")
        } else {
            ErrorField(true, BLANK_EMAIL_ERROR_MESSAGE)
        }
    }

    fun checkAuthCodeValidation(maxLength: Int, hasFocus: Boolean = false) {
        if (hasFocus) return

        val error = validateAuthCodeWithError(maxLength)
        if (error.state.not() && error.msg == CORRECT_AUTH_CODE_MESSAGE) {
            _errorEmail.value = error
        } else {
            _errorAuthCode.value = error
        }
    }

    fun validateAuthCodeWithError(maxLength: Int): ErrorField {
        return if (authCode.value.length == maxLength && correctAuthCode == authCode.value) {
            // 인증 성공
            _authCodeState.value = AuthCodeState.Success
            ErrorField(false, CORRECT_AUTH_CODE_MESSAGE)
        } else if (authCode.value.length == maxLength && correctAuthCode != authCode.value) {
            // 인증 실패
            ErrorField(true, INCORRECT_AUTH_CODE_ERROR_MESSAGE)
        } else {
            ErrorField(false)
        }
    }

    fun checkPasswordValidation(hasFocus: Boolean = false) {
        if (hasFocus) return

        _errorPassword.value = validatePasswordWithError()
    }

    open fun validatePasswordWithError(): ErrorField {
        return if (password.value.isEmpty()) {
            ErrorField(true, BLANK_PASSWORD_ERROR_MESSAGE)
        } else if (Pattern.passwordPattern.matches(password.value).not()) {
            ErrorField(true, NOT_MATCH_PASSWORD_ERROR_MESSAGE)
        } else {
            ErrorField(false)
        }
    }

    fun checkConfirmPasswordValidation(hasFocus: Boolean = false) {
        if (hasFocus) return

        _errorConfirmPassword.value = validateConfirmPasswordWithError()
    }

    fun validateConfirmPasswordWithError(): ErrorField {
        return if (confirmPassword.value.isEmpty()) {
            ErrorField(true, BLANK_PASSWORD_ERROR_MESSAGE)
        } else if (password.value != confirmPassword.value) {
            ErrorField(true, NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE)
        } else {
            ErrorField(false)
        }
    }

    sealed class Event {
        object ShowDialog : Event()
    }
}