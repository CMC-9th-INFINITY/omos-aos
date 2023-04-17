package com.infinity.omos.ui.onboarding.forgot

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentForgotPasswordBinding
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.AuthCodeState
import com.infinity.omos.ui.onboarding.base.OnboardingViewModel
import com.infinity.omos.ui.view.OmosDialog
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false).apply {
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
        initFieldListener()
        initClickListener()
    }

    private fun initFieldListener() {
        binding.ofvEmail.setOnTextChangeListener(errorListener = { text ->
            viewModel.validateEmail(text)
        })

        binding.ofvEmailAuthCode.setOnTextChangeListener(errorListener = { text ->
            viewModel.validateAuthCode(text, resources.getInteger(R.integer.auth_code_length))
        }) { text ->
            if (text.length == resources.getInteger(R.integer.auth_code_length)) {

                if (binding.ofvEmailAuthCode.error == ErrorMessage.NO_ERROR) {
                    viewModel.setAuthCodeState()
                } else {
                    binding.ofvEmailAuthCode.showErrorMessage()
                }
            }
        }
    }

    private fun initClickListener() {
        binding.btnNext.setOnClickListener {
            val directions =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToChangePasswordFragment(viewModel.email.value)
            findNavController().navigate(directions)
        }

        binding.tvSendAuthMail.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun collectData() {
        collectEvent()
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
                    binding.ofvEmail.showSuccessMessage(resources.getString(R.string.success_auth))
                    binding.ofvEmailAuthCode.setEditTextEnabled(false)
                    binding.tvSendAuthMail.visibility = View.GONE
                    viewModel.changeCompleteState(true)
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
                if (binding.ofvEmail.error == ErrorMessage.NO_ERROR) {
                    viewModel.sendAuthMail()
                } else {
                    binding.ofvEmail.showErrorMessage()
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