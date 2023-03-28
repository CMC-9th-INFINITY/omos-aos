package com.infinity.omos.ui.onboarding.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.omos.MainActivity
import com.infinity.omos.databinding.FragmentLoginBinding
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingState.Failure.Companion.NOT_EXIST_USER_ERROR_MESSAGE
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
        initEmailListener()
        initPasswordListener()
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

    private fun initEmailListener() = with(binding.ofvEmail) {
        setOnTextChangeListener { text ->
            viewModel.setEmail(text)
            viewModel.changeLoginState()
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkEmailValidation(hasFocus)
        }
    }

    private fun initPasswordListener() = with(binding.ofvPassword) {
        setOnTextChangeListener { text ->
            viewModel.setPassword(text)
            viewModel.changeLoginState()
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkPasswordValidation(hasFocus)
        }

        setOnPasswordToggleClickListener {
            viewModel.changePasswordVisibleState()
        }
    }

    private fun collectData() {
        collectFieldViewError()
        collectFieldViewPasswordState()
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
                    OnboardingState.Failure(NOT_EXIST_USER_ERROR_MESSAGE) -> {
                        // TODO: 회언가입 닉네임 입력 페이지 이동
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun collectFieldViewError() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorEmail.collect { (state, msg) ->
                binding.ofvEmail.setShowErrorMsg(state, msg)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorPassword.collect { (state, msg) ->
                binding.ofvPassword.setShowErrorMsg(state, msg)
            }
        }
    }

    private fun collectFieldViewPasswordState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.isVisiblePassword.collect { isVisiblePassword ->
                binding.ofvPassword.setShowPassword(isVisiblePassword)
            }
        }
    }
}