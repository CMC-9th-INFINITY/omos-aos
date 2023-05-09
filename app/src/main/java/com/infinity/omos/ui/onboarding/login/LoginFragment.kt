package com.infinity.omos.ui.onboarding.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.omos.ui.main.MainActivity
import com.infinity.omos.databinding.FragmentLoginBinding
import com.infinity.omos.ui.onboarding.error.ErrorMessage
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.utils.KakaoLoginManager
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        collectData()
    }

    private fun initListener() {
        initMoveScreenListener()
        initLoginListener()
        initFieldListener()
    }

    private fun initMoveScreenListener() {
        binding.tvFindPw.setOnClickListener {
            val directions =
                LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            findNavController().navigate(directions)
        }

        binding.tvSignUp.setOnClickListener {
            val directions =
                LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(directions)
        }
    }

    private fun initLoginListener() {
        binding.btnLogin.setOnClickListener {
            viewModel.loginUser()
        }

        binding.btnKakaoLogin.setOnClickListener {
            KakaoLoginManager(requireContext()).login { userId ->
                val userEmail = "$userId@kakao.com"
                viewModel.loginKakaoUser(userEmail)
            }
        }
    }

    private fun initFieldListener() {
        binding.ofvEmail.setOnTextChangeListener(errorListener = { text ->
            viewModel.setEmail(text)
            viewModel.getEmailErrorMessage(text)
        }) {
            changeCompleteState()
        }

        binding.ofvPassword.setOnTextChangeListener(errorListener = { text ->
            viewModel.setPassword(text)
            viewModel.getPasswordErrorMessage(text)
        }) {
            changeCompleteState()
        }
    }

    private fun changeCompleteState() {
        viewModel.changeCompleteState(binding.ofvEmail.error == ErrorMessage.NO_ERROR && binding.ofvPassword.error == ErrorMessage.NO_ERROR)
    }

    private fun collectData() {
        collectLoginState()
    }

    private fun collectLoginState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.state.collect { state ->
                when (state) {
                    OnboardingState.Success -> {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                    OnboardingState.Failure(ErrorMessage.NOT_EXIST_USER_ERROR_MESSAGE) -> {
                        // TODO: 회언가입 닉네임 입력 페이지 이동
                    }
                    else -> Unit
                }
            }
        }
    }
}