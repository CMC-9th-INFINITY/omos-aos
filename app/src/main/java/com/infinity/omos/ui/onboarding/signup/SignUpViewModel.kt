package com.infinity.omos.ui.onboarding.signup

import androidx.lifecycle.ViewModel
import com.infinity.omos.ui.onboarding.OnboardingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel : ViewModel() {

    private var _isActivatedNext = MutableStateFlow(false)
    val isActivatedNext = _isActivatedNext.asStateFlow()

    private var _state = MutableStateFlow<OnboardingState>(OnboardingState.Nothing)
    val state = _state.asStateFlow()


}