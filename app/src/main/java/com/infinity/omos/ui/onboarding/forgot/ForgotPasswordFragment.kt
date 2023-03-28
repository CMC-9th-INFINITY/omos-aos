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
        binding.ofvEmail.setOnTextChangeListener { text ->
            viewModel.setEmail(text)
        }

        binding.ofvEmailAuthCode.setOnTextChangeListener { text ->
            viewModel.setAuthCode(text)
            val isCompleted = viewModel.isValidAuthCode(resources.getInteger(R.integer.auth_code_length))
            if (isCompleted) {
                viewModel.changeCompleteState()
            }
        }

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
        collectFieldViewText()
        collectFieldViewError()
        collectEvent()
    }

    private fun collectFieldViewText() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.authCode.collect { authCode ->
                binding.ofvEmailAuthCode.text = authCode
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
            viewModel.errorAuthCode.collect { (state, msg) ->
                binding.ofvEmailAuthCode.setShowErrorMsg(state, msg)
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
            viewModel.isCompleted.collect { isCompleted ->
                if (isCompleted) {
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
            title = "인증메일을 발송하시겠습니까?",
            okText = "발송",
            onOkClickListener = {
                viewModel.isValidEmail()

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