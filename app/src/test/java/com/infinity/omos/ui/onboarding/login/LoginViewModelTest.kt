package com.infinity.omos.ui.onboarding.login

import com.google.common.truth.Truth.assertThat
import com.infinity.omos.data.user.UserToken
import com.infinity.omos.repository.UserRepository
import com.infinity.omos.ui.MainDispatcherRule
import com.infinity.omos.ui.onboarding.OnboardingState
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