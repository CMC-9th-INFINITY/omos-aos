package com.infinity.omos.ui.setting.change.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentChangePasswordBinding
import com.infinity.omos.ui.onboarding.ErrorMessage
import com.infinity.omos.ui.onboarding.base.OnboardingState
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    private val args: ChangePasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false).apply {
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
        initBackButtonListener()
        initFieldListener()
        initCompleteListener()
    }

    private fun initBackButtonListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initFieldListener() {
        binding.ofvPassword.setOnTextChangeListener(errorListener = { text ->
            viewModel.setPassword(text)
            viewModel.getPasswordErrorMessage(text)
        }) {
            if (viewModel.getConfirmPasswordErrorMessage() == ErrorMessage.NO_ERROR) {
                binding.ofvConfirmPassword.hideErrorMessage()
            }

            changeCompleteState()
        }

        binding.ofvConfirmPassword.setOnTextChangeListener(errorListener = { text ->
            viewModel.setConfirmPassword(text)
            viewModel.getConfirmPasswordErrorMessage(text)
        }) {
            changeCompleteState()
        }
    }

    private fun changeCompleteState() {
        viewModel.changeCompleteState(binding.ofvPassword.error == ErrorMessage.NO_ERROR && binding.ofvConfirmPassword.error == ErrorMessage.NO_ERROR)
    }

    private fun initCompleteListener() {
        binding.btnComplete.setOnClickListener {
            viewModel.changePassword(args.email)
        }
    }

    private fun collectData() {
        collectLoginState()
    }

    private fun collectLoginState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.state.collect { state ->
                if (state == OnboardingState.Success) {
                    val directions =
                        ChangePasswordFragmentDirections.actionChangePasswordFragmentToLoginFragment()
                    findNavController().navigate(directions)

                    Toast.makeText(context, getString(R.string.change_password), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}