package com.infinity.omos.ui.onboarding.signup

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentSignUpBinding
import com.infinity.omos.ui.onboarding.base.AuthCodeState
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import com.infinity.omos.ui.view.OmosDialog
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false).apply {
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
        initEmailListener()
        initAuthCodeListener()
        initPasswordListener()
        initConfirmPasswordListener()
        initButtonClickListener()
    }

    private fun initEmailListener() = with(binding.ofvEmail) {
        setOnTextChangeListener { email ->
            viewModel.setEmail(email)
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkEmailValidation(hasFocus)
        }
    }

    private fun initAuthCodeListener() = with(binding.ofvEmailAuthCode) {
        setOnTextChangeListener { text ->
            viewModel.setAuthCode(text)
            viewModel.checkAuthCodeValidation(resources.getInteger(R.integer.auth_code_length))
            viewModel.changeCompleteState()
        }

        setOnFocusChangeListener {
            viewModel.checkAuthCodeValidation(resources.getInteger(R.integer.auth_code_length))
        }
    }

    private fun initPasswordListener() = with(binding.ofvPassword) {
        setOnTextChangeListener { text ->
            viewModel.setPassword(text)
            viewModel.changeCompleteState()

            if (viewModel.confirmPassword.value.isNotEmpty()) {
                viewModel.checkConfirmPasswordValidation()
            }
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkPasswordValidation(hasFocus)
        }
    }

    private fun initConfirmPasswordListener() = with(binding.ofvConfirmPassword) {
        setOnTextChangeListener { text ->
            viewModel.setConfirmPassword(text)
            viewModel.changeCompleteState()
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkConfirmPasswordValidation(hasFocus)
        }
    }
    private fun initButtonClickListener() {
        binding.tvSendAuthMail.setOnClickListener {
            showAlertDialog()
        }

        binding.btnNext.setOnClickListener {
            val directions =
                SignUpFragmentDirections.actionSignUpFragmentToNicknameFragment(viewModel.email.value, viewModel.password.value)
            findNavController().navigate(directions)
        }
    }

    private fun collectData() {
        collectFieldViewError()
        collectEvent()
    }

    private fun collectFieldViewError() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorEmail.collect { (state, msg) ->
                binding.ofvEmail.setShowErrorMsg(state, msg)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorAuthCode.collect { (state, msg) ->
                binding.ofvEmailAuthCode.setShowErrorMsg(state, msg)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorPassword.collect { (state, msg) ->
                binding.ofvPassword.setShowErrorMsg(state, msg)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorConfirmPassword.collect { (state, msg) ->
                binding.ofvConfirmPassword.setShowErrorMsg(state, msg)
            }
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.event.collect { event ->
                if (event is OnboardingViewModel.Event.ShowDialog) {
                    showSuccessDialog()
                    setAgainMail()
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.authCodeState.collect { authCodeState ->
                if (authCodeState is AuthCodeState.Success) {
                    binding.ofvEmail.setEditTextEnabled(false)
                    binding.ofvEmailAuthCode.setEditTextEnabled(false)
                    binding.tvSendAuthMail.visibility = View.GONE
                }
            }
        }
    }

    private fun setAgainMail() {
        binding.tvSendAuthMail.text = requireContext().getString(R.string.send_again_auth_mail)
        binding.tvSendAuthMail.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun showAlertDialog() {
        val dialog = OmosDialog(requireActivity())
        dialog.showDialog(
            title = getString(R.string.alert_send_email),
            okText = getString(R.string.send),
            onOkClickListener = {
                viewModel.checkEmailValidation()

                if (viewModel.errorEmail.value.state.not()) {
                    viewModel.sendAuthMail()
                }
            }
        )
    }

    private fun showSuccessDialog() {
        val dialog = OmosDialog(requireActivity())
        dialog.showDialog(
            title = getString(R.string.complete_send_auth_mail),
            okText = getString(R.string.ok),
            cancelVisible = false,
            onOkClickListener = null
        )
    }
}