package com.infinity.omos.ui.setting.change.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserPassword
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.OnboardingState
import com.infinity.omos.ui.onboarding.OnboardingState.Failure.Companion.NETWORK_ERROR_MESSAGE
import com.infinity.omos.utils.Pattern.Companion.passwordPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private var _confirmNewPassword = MutableStateFlow("")
    val confirmNewPassword = _confirmNewPassword.asStateFlow()

    private var _errorNewPassword = MutableStateFlow(ErrorField(false))
    val errorNewPassword = _errorNewPassword.asStateFlow()

    private var _errorConfirmNewPassword = MutableStateFlow(ErrorField(false))
    val errorConfirmNewPassword = _errorConfirmNewPassword.asStateFlow()

    private var _isVisibleNewPassword = MutableStateFlow(false)
    val isVisibleNewPassword = _isVisibleNewPassword.asStateFlow()

    private var _isVisibleConfirmNewPassword = MutableStateFlow(false)
    val isVisibleConfirmNewPassword = _isVisibleConfirmNewPassword.asStateFlow()

    private var _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    fun setNewPassword(password: String) {
        if (errorNewPassword.value.state) {
            _errorNewPassword.value = ErrorField(false)
        }
        _newPassword.value = password
    }

    fun setConfirmNewPassword(password: String) {
        if (errorConfirmNewPassword.value.state) {
            _errorConfirmNewPassword.value = ErrorField(false)
        }
        _confirmNewPassword.value = password
    }

    fun changeNewPasswordVisibleState() {
        _isVisibleNewPassword.value = isVisibleNewPassword.value.not()
    }

    fun changeConfirmNewPasswordVisibleState() {
        _isVisibleConfirmNewPassword.value = isVisibleConfirmNewPassword.value.not()
    }

    fun changeCompleteState() {
        _isCompleted.value =
            passwordPattern.matches(confirmNewPassword.value) && newPassword.value == confirmNewPassword.value
    }

    fun checkNewPasswordValidation(hasFocus: Boolean) {
        if (hasFocus.not()) {
            _errorNewPassword.value = if (newPassword.value.isEmpty()) {
                ErrorField(
                    true,
                    OnboardingState.Failure.BLANK_PASSWORD_ERROR_MESSAGE
                )
            } else if (passwordPattern.matches(newPassword.value).not()) {
                ErrorField(
                    true,
                    OnboardingState.Failure.NOT_MATCH_PASSWORD_ERROR_MESSAGE
                )
            } else {
                ErrorField(false)
            }
        }
    }

    fun checkConfirmNewPasswordValidation(hasFocus: Boolean) {
        if (hasFocus.not()) {
            _errorConfirmNewPassword.value = if (confirmNewPassword.value.isEmpty()) {
                ErrorField(
                    true,
                    OnboardingState.Failure.BLANK_PASSWORD_ERROR_MESSAGE
                )
            } else if (newPassword.value != confirmNewPassword.value) {
                ErrorField(
                    true,
                    OnboardingState.Failure.NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE
                )
            } else {
                ErrorField(false)
            }
        }
    }

    fun changePassword() {
        _state.value = OnboardingState.Loading
        viewModelScope.launch {
            val password = UserPassword(confirmNewPassword.value, 0)
            userRepository.changePassword(password)
                .onSuccess { _state.value = OnboardingState.Success }
                .onFailure { _state.value = OnboardingState.Failure(NETWORK_ERROR_MESSAGE) }
        }
    }
}