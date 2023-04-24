package com.infinity.omos.ui.onboarding

import com.google.common.truth.Truth.assertThat
import com.infinity.omos.data.user.UserToken
import com.infinity.omos.repository.user.UserRepository
import com.infinity.omos.ui.MainDispatcherRule
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.login.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class LoginViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(userRepository)
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
    fun `비밀번호 비어있을 때 에러 메시지 반환`() {
        // given
        val text = ""

        // when
        val result = viewModel.getPasswordErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.BLANK_PASSWORD_ERROR_MESSAGE)
    }

    @Test
    fun `이메일 양식에 맞을 때 성공 메시지 반환`() {
        // given
        val text = "email@naver.com"

        // when
        val result = viewModel.getEmailErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.NO_ERROR)
    }

    @Test
    fun `비밀번호 입력 시 성공 메시지 반환`() {
        // given
        val text = "password"

        // when
        val result = viewModel.getPasswordErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.NO_ERROR)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `로그인 성공 시 Success 상태가 되는가`() = runTest {
        // given
        val token = UserToken("abc", "def", 0)
        val resultToken = Result.success(token)
        coEvery { userRepository.loginUser(any()) } returns resultToken
        coEvery { userRepository.saveToken(any()) } returns Unit

        // when
        viewModel.loginUser()
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Success::class.java)
        coVerify { userRepository.saveToken(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `로그인 실패 시 Failure 상태가 되는가`() = runTest {
        val exception = mockk<HttpException>()
        val resultToken = Result.failure<UserToken>(exception)
        coEvery { userRepository.loginUser(any()) } returns resultToken
        coEvery { userRepository.saveToken(any()) } returns Unit

        // when
        viewModel.loginUser()
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Failure::class.java)
        coVerify(exactly = 0) { userRepository.saveToken(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `카카오 로그인 성공 시 Success 상태가 되는가`() = runTest {
        // given
        val token = UserToken("abc", "def", 0)
        val resultToken = Result.success(token)
        coEvery { userRepository.loginSnsUser(any()) } returns resultToken
        coEvery { userRepository.saveToken(any()) } returns Unit

        // when
        viewModel.loginKakaoUser("email@naver.com")
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Success::class.java)
        coVerify { userRepository.saveToken(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `카카오 로그인 실패 시 Failure 상태가 되는가`() = runTest {
        val exception = mockk<HttpException>()
        val resultToken = Result.failure<UserToken>(exception)
        coEvery { userRepository.loginSnsUser(any()) } returns resultToken
        coEvery { userRepository.saveToken(any()) } returns Unit

        // when
        viewModel.loginKakaoUser("email@naver.com")
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Failure::class.java)
        coVerify(exactly = 0) { userRepository.saveToken(any()) }
    }
}