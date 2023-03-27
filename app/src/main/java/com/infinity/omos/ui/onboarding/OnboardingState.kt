package com.infinity.omos.ui.onboarding

sealed class OnboardingState{

    object Nothing : OnboardingState()

    object Loading : OnboardingState()

    object Success : OnboardingState()

    data class Failure(val msg: String) : OnboardingState() {

        companion object {
            const val INCORRECT_CONTENTS_ERROR_MESSAGE = "입력하신 내용을 다시 확인해주세요."
            const val BLANK_EMAIL_ERROR_MESSAGE = "이메일을 입력해주세요."
            const val BLANK_PASSWORD_ERROR_MESSAGE = "비밀번호를 입력해주세요."
            const val NOT_EXIST_USER_ERROR_MESSAGE = "해당하는 유저가 존재하지 않습니다."
            const val INCORRECT_AUTH_CODE_ERROR_MESSAGE = "인증코드가 일치하지 않습니다."
            const val NETWORK_ERROR_MESSAGE = "네트워크 오류"
        }
    }
}
