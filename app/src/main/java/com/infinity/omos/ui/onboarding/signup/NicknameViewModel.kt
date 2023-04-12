package com.infinity.omos.ui.onboarding.signup

import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserSignUp
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.ALREADY_EXIST_NICKNAME
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val userRepository: UserRepository
) : OnboardingViewModel() {

    private var _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private var _tosState = MutableStateFlow(false)
    val tosState = _tosState.asStateFlow()

    private var _ppState = MutableStateFlow(false)
    val ppState = _ppState.asStateFlow()

    private var _errorNick = MutableStateFlow(ErrorField(false))
    val errorNick = _errorNick.asStateFlow()

    fun setTosCheckBox(state: Boolean) {
        _tosState.value = state
    }

    fun setPpCheckBox(state: Boolean) {
        _ppState.value = state
    }

    fun setNickname(nickname: String) {
        if (errorNick.value.state) {
            _errorNick.value = ErrorField(false)
        }
        _nickname.value = nickname
    }

    override fun changeCompleteState() {
        _isCompleted.value = tosState.value && ppState.value && nickname.value.isNotEmpty()
    }

    fun signUp(email: String, password: String) {
        _state.value = OnboardingState.Loading
        viewModelScope.launch {
            val user = UserSignUp(email, nickname.value, password)
            userRepository.signUpUser(user)
                .onSuccess {
                    _state.value = OnboardingState.Success
                }
                .onFailure {
                    _errorNick.value = ErrorField(true, ALREADY_EXIST_NICKNAME)
                    _state.value = OnboardingState.Failure(OnboardingState.Failure.NETWORK_ERROR_MESSAGE)
                }
        }
    }
}