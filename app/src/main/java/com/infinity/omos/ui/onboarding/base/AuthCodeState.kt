package com.infinity.omos.ui.onboarding.base

sealed class AuthCodeState {

    object Nothing : AuthCodeState()

    object Ready : AuthCodeState()

    object Success : AuthCodeState()
}