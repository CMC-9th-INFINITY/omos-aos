package com.infinity.omos.ui.onboarding.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
        collectState()
    }

    private fun initListener() {
        initMoveScreenListener()
        initLoginListener()
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

    private fun collectState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isVisiblePassword.collect { isVisiblePassword ->
                    binding.ivEye.isSelected = isVisiblePassword
                    binding.etPw.transformationMethod = if (isVisiblePassword) {
                        HideReturnsTransformationMethod.getInstance()
                    } else {
                        PasswordTransformationMethod.getInstance()
                    }
                    binding.etPw.setSelection(viewModel.password.value.length)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorEmail.collect { errorEmail ->
                    binding.etEmail.isActivated = errorEmail.state
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorPassword.collect { errorPassword ->
                    binding.constraintPassword.isActivated = errorPassword.state
                }
            }
        }
    }
}