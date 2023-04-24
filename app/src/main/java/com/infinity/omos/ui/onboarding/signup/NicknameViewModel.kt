package com.infinity.omos.ui.onboarding.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity.omos.data.user.UserSignUp
import com.infinity.omos.repository.user.UserRepository
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.OnboardingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private var _tosState = MutableStateFlow(false)
    val tosState = _tosState.asStateFlow()

    private var _ppState = MutableStateFlow(false)
    val ppState = _ppState.asStateFlow()

    private var _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()

    fun setTosCheckBox(state: Boolean) {
        _tosState.value = state
    }

    fun setPpCheckBox(state: Boolean) {
        _ppState.value = state
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    fun changeCompleteState() {
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
                    _state.value = OnboardingState.Failure(ErrorMessage.ALREADY_EXIST_NICKNAME_ERROR_MESSAGE)
                }
        }
    }
}