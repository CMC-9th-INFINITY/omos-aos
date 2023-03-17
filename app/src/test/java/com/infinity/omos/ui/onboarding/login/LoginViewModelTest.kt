package com.infinity.omos.ui.onboarding.login

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel()
    }

    @Test
    fun `이메일과 비밀번호에 텍스트 입력 시 로그인 버튼 활성화`() {
        // given
        viewModel.setEmail("email")
        viewModel.setPassword("password")

        // when
        viewModel.changeLoginState()
        val result = viewModel.isActivatedLogin.value

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `이메일 또는 비밀번호 공백 시 로그인 버튼 비활성화`() {
        // given
        viewModel.setEmail("")
        viewModel.setPassword("")

        // when
        viewModel.changeLoginState()
        val result = viewModel.isActivatedLogin.value

        // then
        assertThat(result).isFalse()
    }

    @Test
    fun `이메일 입력란이 비어있을 때 오류 발생`() {
        // given
        viewModel.setEmail("")

        // when
        viewModel.checkEmailValidation(false)
        val error = viewModel.errorEmail.value

        // then
        assertThat(error.state).isTrue()
        assertThat(error.msg).isEqualTo("이메일을 입력해주세요.")
    }

    @Test
    fun `이메일 형식이 아닐 때 오류 발생`() {
        // given
        viewModel.setEmail("abcde")

        // when
        viewModel.checkEmailValidation(false)
        val error = viewModel.errorEmail.value

        // then
        assertThat(error.state).isTrue()
        assertThat(error.msg).isEqualTo("입력하신 내용을 다시 확인해주세요.")
    }

    @Test
    fun `비밀번호 입력란이 비어있을 때 오류 발생`() {
        // given
        viewModel.setPassword("")

        // when
        viewModel.checkPasswordValidation(false)
        val error = viewModel.errorPassword.value

        // then
        assertThat(error.state).isTrue()
        assertThat(error.msg).isEqualTo("비밀번호를 입력해주세요.")
    }
}