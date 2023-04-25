package com.infinity.omos.ui.onboarding

import com.google.common.truth.Truth.assertThat
import com.infinity.omos.data.NetworkResult
import com.infinity.omos.repository.user.UserRepository
import com.infinity.omos.ui.MainDispatcherRule
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.signup.NicknameViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NicknameViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NicknameViewModel

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = NicknameViewModel(userRepository)
    }

    @Test
    fun `이미 존재하는 닉네임일 때 Failure 상태가 되는가`() {
        // given
        val exception = Exception()
        coEvery { userRepository.signUpUser(any()) } returns Result.failure(exception)

        // when
        viewModel.signUp("email", "password")
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Failure::class.java)
    }

    @Test
    fun `회원가입 성공 시 Success 상태가 되는가`() {
        // given
        coEvery { userRepository.signUpUser(any()) } returns Result.success(NetworkResult(true))

        // when
        viewModel.signUp("email", "password")
        val result = viewModel.state.value

        // then
        assertThat(result).isInstanceOf(OnboardingState.Success::class.java)
    }
}