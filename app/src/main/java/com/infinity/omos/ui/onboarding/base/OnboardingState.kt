package com.infinity.omos.ui.onboarding.base

import com.infinity.omos.ui.onboarding.ErrorMessage

sealed class OnboardingState{

    object Nothing : OnboardingState()

    object Loading : OnboardingState()

    object Success : OnboardingState()

    data class Failure(val error: ErrorMessage) : OnboardingState()
}
