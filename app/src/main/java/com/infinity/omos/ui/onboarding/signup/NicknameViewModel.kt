package com.infinity.omos.ui.onboarding.signup

import com.infinity.omos.ui.onboarding.ErrorField
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NicknameViewModel : OnboardingViewModel() {

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
        _isCompleted.value = tosState.value && ppState.value
    }
}