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
        initNewPasswordListener()
        initConfirmNewPasswordListener()
        initCompleteListener()
    }

    private fun initBackButtonListener() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initNewPasswordListener() = with(binding.ofvNewPassword) {
        setOnTextChangeListener { text ->
            viewModel.setNewPassword(text)
            viewModel.changeCompleteState()

            if (viewModel.confirmNewPassword.value.isNotEmpty()) {
                viewModel.checkConfirmNewPasswordValidation(false)
            }
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkNewPasswordValidation(hasFocus)
        }

        setOnPasswordToggleClickListener {
            viewModel.changeNewPasswordVisibleState()
        }
    }

    private fun initConfirmNewPasswordListener() = with(binding.ofvConfirmNewPassword) {
        setOnTextChangeListener { text ->
            viewModel.setConfirmNewPassword(text)
            viewModel.changeCompleteState()
        }

        setOnFocusChangeListener { hasFocus ->
            viewModel.checkConfirmNewPasswordValidation(hasFocus)
        }

        setOnPasswordToggleClickListener {
            viewModel.changeConfirmNewPasswordVisibleState()
        }
    }

    private fun initCompleteListener() {
        binding.btnComplete.setOnClickListener {
            viewModel.changePassword(args.email)
        }
    }

    private fun collectData() {
        collectFieldViewError()
        collectFieldViewPasswordState()
        collectLoginState()
    }

    private fun collectFieldViewError() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorNewPassword.collect { (state, msg) ->
                binding.ofvNewPassword.setShowErrorMsg(state, msg)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.errorConfirmNewPassword.collect { (state, msg) ->
                binding.ofvConfirmNewPassword.setShowErrorMsg(state, msg)
            }
        }
    }

    private fun collectFieldViewPasswordState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.isVisibleNewPassword.collect { isVisibleNewPassword ->
                binding.ofvNewPassword.setShowPassword(isVisibleNewPassword)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.isVisibleConfirmNewPassword.collect { isVisibleConfirmNewPassword ->
                binding.ofvConfirmNewPassword.setShowPassword(isVisibleConfirmNewPassword)
            }
        }
    }

    private fun collectLoginState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.state.collect { state ->
                if (state == OnboardingState.Success) {
                    val directions =
                        ChangePasswordFragmentDirections.actionChangePasswordFragmentToLoginFragment()
                    findNavController().navigate(directions)

                    Toast.makeText(context, getString(R.string.change_password), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}