package com.infinity.omos.ui.onboarding.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.infinity.omos.MainActivity
import com.infinity.omos.data.UserSnsLogin
import com.infinity.omos.databinding.FragmentLoginBinding
import com.infinity.omos.ui.onboarding.login.LoginState.Failure.Companion.NOT_EXIST_USER
import com.infinity.omos.utils.KakaoLoginManager
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
                LoginFragmentDirections.actionLoginFragmentToFindPwFragment()
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
        collectFieldViewText()
        collectFieldViewError()
        collectFieldViewPasswordState()
        collectLoginState()
    }

    private fun collectLoginState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.state.collect { state ->
                when (state) {
                    LoginState.Success -> {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }
                    LoginState.Failure(NOT_EXIST_USER) -> {
                        // TODO: 회언가입 닉네임 입력 페이지 이동
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun collectFieldViewText() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.email.collect { email ->
                if (binding.ofvEmail.text != email) {
                    binding.ofvEmail.text = email
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.password.collect { password ->
                if (binding.ofvPassword.text != password) {
                    binding.ofvPassword.text = password
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