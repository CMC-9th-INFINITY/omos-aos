package com.infinity.omos.ui.onboarding.login

sealed class LoginState{

    object Nothing : LoginState()

    object Loading : LoginState()

    object Success : LoginState()

    object Failure : LoginState()
}
