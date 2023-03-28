package com.infinity.omos.ui.setting.change.password

import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserId
import com.infinity.omos.data.user.UserPassword
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.NETWORK_ERROR_MESSAGE
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : OnboardingViewModel() {

    override fun changeCompleteState() {
        _isCompleted.value =
            validatePasswordWithError().state.not() && password.value == confirmPassword.value
    }

    fun changePassword(email: String) {
        _state.value = OnboardingState.Loading
        viewModelScope.launch {
            val userId = userRepository.getUserIdFromEmail(email).getOrDefault(UserId(-1))
            val password = UserPassword(confirmPassword.value, userId.userId)
            userRepository.changePassword(password)
                .onSuccess { _state.value = OnboardingState.Success }
                .onFailure { _state.value = OnboardingState.Failure(NETWORK_ERROR_MESSAGE) }
        }
    }
}