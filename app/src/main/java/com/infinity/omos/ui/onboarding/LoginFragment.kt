package com.infinity.omos.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentLoginBinding
import com.infinity.omos.viewmodels.onboarding.OnBoardingViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: OnBoardingViewModel by navGraphViewModels(R.id.nav_on_boarding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}