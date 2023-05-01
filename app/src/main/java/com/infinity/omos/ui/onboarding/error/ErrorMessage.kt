package com.infinity.omos.ui.onboarding.error

enum class ErrorMessage(val msg: String?) {
    DEFAULT(null),
    NO_ERROR(""),
    INCORRECT_CONTENTS_ERROR_MESSAGE("입력하신 내용을 다시 확인해주세요."),
    BLANK_EMAIL_ERROR_MESSAGE("이메일을 입력해주세요."),
    BLANK_PASSWORD_ERROR_MESSAGE("비밀번호를 입력해주세요."),
    NOT_MATCH_PASSWORD_ERROR_MESSAGE("8~16자의 영문 대소문자, 숫자, 특수문자만 가능해요."),
    NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE("비밀번호가 일치하지 않아요."),
    NOT_EXIST_USER_ERROR_MESSAGE("해당하는 유저가 존재하지 않습니다."),
    INCORRECT_AUTH_CODE_ERROR_MESSAGE("인증코드가 일치하지 않습니다."),
    NETWORK_ERROR_MESSAGE("네트워크 오류"),
    ALREADY_EXIST_NICKNAME_ERROR_MESSAGE("이미 쓰고 있는 닉네임이에요."),
    ALREADY_EXIST_EMAIL_ERROR_MESSAGE("이미 쓰고 있는 이메일이에요."),
}