package com.infinity.omos.ui.onboarding

import com.google.common.truth.Truth.assertThat
import com.infinity.omos.data.auth.AuthCode
import com.infinity.omos.repository.auth.AuthRepository
import com.infinity.omos.ui.MainDispatcherRule
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.error.ErrorMessage
import com.infinity.omos.ui.onboarding.forgot.ForgotPasswordViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgotPasswordViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ForgotPasswordViewModel

    @MockK
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ForgotPasswordViewModel(authRepository)
    }

    @Test
    fun `이메일이 비어있을 때 에러 메시지 반환`() {
        // given
        val text = ""

        // when
        val result = viewModel.getEmailErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.BLANK_EMAIL_ERROR_MESSAGE)
    }

    @Test
    fun `이메일 양식이 틀릴 때 에러 메시지 반환`() {
        // given
        val text = "email"

        // when
        val result = viewModel.getEmailErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.INCORRECT_CONTENTS_ERROR_MESSAGE)
    }

    @Test
    fun `이메일 양식이 일치할 때 성공 메시지 반환`() {
        // given
        val text = "email@naver.com"

        // when
        val result = viewModel.getEmailErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.NO_ERROR)
    }

    @Test
    fun `인증코드가 일치하지 않을 때 에러 메시지 반환`() {
        // given
        val input = "123456"
        val authCode = "000000"
        viewModel.setAuthCode(authCode)

        // when
        val result = viewModel.getAuthCodeErrorMessage(input, 6)

        // then
        assertThat(result).isEqualTo(ErrorMessage.INCORRECT_AUTH_CODE_ERROR_MESSAGE)
    }

    @Test
    fun `인증코드가 일치할 때 성공 메시지 반환`() {
        // given
        val input = "123456"
        val authCode = "123456"
        coEvery { authRepository.sendAuthMail(any()) } returns Result.success(AuthCode(authCode))

        // when
        viewModel.sendAuthMail()
        val result = viewModel.getAuthCodeErrorMessage(input, 6)

        // then
        assertThat(result).isEqualTo(ErrorMessage.NO_ERROR)
    }

    @Test
    fun `메일로 인증코드를 성공적으로 보냈을 때 Success 상태가 되는가`() {
        // given
        val authCode = "123456"
        coEvery { authRepository.sendAuthMail(any()) } returns Result.success(AuthCode(authCode))

        // when
        viewModel.sendAuthMail()
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Success::class.java)
    }

    @Test
    fun `메일로 인증코드를 보내는 것을 실패했을 때 Failure 상태가 되는가`() {
        // given
        val exception = Exception()
        coEvery { authRepository.sendAuthMail(any()) } returns Result.failure(exception)

        // when
        viewModel.sendAuthMail()
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Failure::class.java)
    }
}