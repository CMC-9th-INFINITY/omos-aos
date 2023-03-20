package com.infinity.omos.ui.onboarding.login

sealed class LoginState{

    object Nothing : LoginState()

    object Loading : LoginState()

    object Success : LoginState()

    data class Failure(val msg: String) : LoginState() {

        companion object {
            const val INCORRECT_CONTENTS_ERROR_MESSAGE = "입력하신 내용을 다시 확인해주세요."
            const val BLANK_EMAIL_ERROR_MESSAGE = "이메일을 입력해주세요."
            const val BLANK_PASSWORD_ERROR_MESSAGE = "비밀번호를 입력해주세요."
            const val NOT_EXIST_USER = "해당하는 유저가 존재하지 않습니다."
        }
    }
}
