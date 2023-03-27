package com.infinity.omos.ui.onboarding.forgot

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentForgotPasswordBinding
import com.infinity.omos.ui.onboarding.OnboardingState
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
            viewModel.changeNextState()
        }

        binding.ofvEmail.setOnFocusChangeListener { hasFocus ->
            viewModel.checkEmailValidation(hasFocus)
        }

        binding.btnNext.setOnClickListener {
            // TODO: 비밀번호 변경 페이지 이동
        }

        binding.tvSendAuthMail.setOnClickListener {
            viewModel.sendAuthMail()
        }
    }

    private fun collectData() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorEmail.collect { (state, msg) ->
                binding.ofvEmail.setShowErrorMsg(state, msg)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.state.collect { state ->
                if (state == OnboardingState.Success) {
                    // TODO: 인증메일 발송 성공 팝업 띄우기
                    setAgainMail()
                }
            }
        }
    }

    private fun setAgainMail() {
        binding.tvSendAuthMail.text = requireContext().getString(R.string.send_again_auth_mail)
        binding.tvSendAuthMail.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}