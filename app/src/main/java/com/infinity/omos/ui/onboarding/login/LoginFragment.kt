package com.infinity.omos.ui.onboarding.login

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
import com.infinity.omos.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

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
        /*binding.btnLogin.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }*/
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
    }

    private fun collectFieldViewText() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.email.collect { email ->
                    if (binding.ofvEmail.text != email) {
                        binding.ofvEmail.text = email
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.password.collect { password ->
                    if (binding.ofvPassword.text != password) {
                        binding.ofvPassword.text = password
                    }
                }
            }
        }
    }

    private fun collectFieldViewError() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorEmail.collect { errorEmail ->
                    binding.ofvEmail.setShowErrorMsg(errorEmail)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorPassword.collect { errorPassword ->
                    binding.ofvPassword.setShowErrorMsg(errorPassword)
                }
            }
        }
    }

    private fun collectFieldViewPasswordState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isVisiblePassword.collect { isVisiblePassword ->
                    binding.ofvPassword.setShowPassword(isVisiblePassword)
                }
            }
        }
    }
}