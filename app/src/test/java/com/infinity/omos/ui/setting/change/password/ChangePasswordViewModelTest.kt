package com.infinity.omos.ui.setting.password

import com.google.common.truth.Truth.assertThat
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.data.user.UserId
import com.infinity.omos.repository.user.UserRepository
import com.infinity.omos.ui.MainDispatcherRule
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.setting.change.password.ChangePasswordViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChangePasswordViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ChangePasswordViewModel

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChangePasswordViewModel(userRepository)
    }

    @Test
    fun `비밀번호가 비어 있을 때 에러 메시지 반환`(){
        // given
        val text = ""

        // when
        val result = viewModel.getPasswordErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.BLANK_PASSWORD_ERROR_MESSAGE)
    }

    @Test
    fun `비밀번호 양식이 일치하지 않을 때 에러 메시지 반환`() {
        // given
        val text = "password"

        // when
        val result = viewModel.getPasswordErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.NOT_MATCH_PASSWORD_ERROR_MESSAGE)
    }

    @Test
    fun `비밀번호 양식이 일치할 때 성공 메시지 반환`() {
        // given
        val text = "password123!"

        // when
        val result = viewModel.getPasswordErrorMessage(text)

        // then
        assertThat(result).isEqualTo(ErrorMessage.NO_ERROR)
    }

    @Test
    fun `비밀번호와 비밀번호 재확인이 동일하지 않을 때 에러 메시지 반환`() {
        // given
        val password = "aaaa"
        val confirmPassword = "bbbb"
        viewModel.setPassword(password)
        viewModel.setConfirmPassword(confirmPassword)

        // when
        val result = viewModel.getConfirmPasswordErrorMessage()

        // then
        assertThat(result).isEqualTo(ErrorMessage.NOT_MATCH_CONFIRM_PASSWORD_ERROR_MESSAGE)
    }

    @Test
    fun `비밀번호와 비밀번호 재확인이 동일할 때 성공 메시지 반환`() {
        val password = "aaaa"
        val confirmPassword = "aaaa"
        viewModel.setPassword(password)
        viewModel.setConfirmPassword(confirmPassword)

        // when
        val result = viewModel.getConfirmPasswordErrorMessage()

        // then
        assertThat(result).isEqualTo(ErrorMessage.NO_ERROR)
    }

    @Test
    fun `비밀번호 변경 성공 시 Success 상태가 되는가`() {
        // given
        coEvery { userRepository.getUserIdFromEmail(any()) } returns Result.success(UserId(1))
        coEvery { userRepository.changePassword(any()) } returns Result.success(NetworkResult(true))

        // when
        viewModel.changePassword("email")
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Success::class.java)
    }

    @Test
    fun `비밀번호 변경 실패 시 Failure 상태가 되는가`() {
        // given
        val exception = Exception()
        coEvery { userRepository.getUserIdFromEmail(any()) } returns Result.success(UserId(1))
        coEvery { userRepository.changePassword(any()) } returns Result.failure(exception)

        // when
        viewModel.changePassword("email")
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Failure::class.java)
    }
}