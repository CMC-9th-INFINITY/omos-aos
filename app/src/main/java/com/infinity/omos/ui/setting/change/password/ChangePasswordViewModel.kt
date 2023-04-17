package com.infinity.omos.ui.setting.change.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserId
import com.infinity.omos.data.user.UserPassword
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.utils.Pattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private var _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private var _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    fun changeCompleteState(state: Boolean) {
        _isCompleted.value = state
    }

    fun setPassword(text: String) {
        _password.value = text
    }

    fun getPasswordErrorMessage(text: String): ErrorMessage {
        return if (text.isEmpty()) {
            ErrorMessage.BLANK_PASSWORD_ERROR_MESSAGE
        } else if (Pattern.passwordPattern.matches(text).not()) {
            ErrorMessage.NOT_MATCH_PASSWORD_ERROR_MESSAGE
        } else {
            ErrorMessage.NO_ERROR
        }
    }

    fun setConfirmPassword(text: String) {
        _confirmPassword.value = text
    }

    fun getConfirmPasswordErrorMessage(text: String = confirmPassword.value): ErrorMessage {
        return if (password.value != text) {
            ErrorMessage.NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE
        } else {
            ErrorMessage.NO_ERROR
        }
    }

    fun changePassword(email: String) {
        _state.value = OnboardingState.Loading
        viewModelScope.launch {
            val userId = userRepository.getUserIdFromEmail(email).getOrDefault(UserId(-1))
            val password = UserPassword(confirmPassword.value, userId.userId)
            userRepository.changePassword(password)
                .onSuccess { _state.value = OnboardingState.Success }
                .onFailure { _state.value = OnboardingState.Failure(ErrorMessage.NETWORK_ERROR_MESSAGE) }
        }
    }
}