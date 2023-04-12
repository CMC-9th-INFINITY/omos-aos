package com.infinity.omos.ui.onboarding.signup

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentNicknameBinding
import com.infinity.omos.ui.view.OmosDialog

class NicknameFragment : Fragment() {

    private lateinit var binding: FragmentNicknameBinding
    private val viewModel: NicknameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNicknameBinding.inflate(inflater, container, false).apply {
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
        initNicknameListener()
        initCheckBoxListener()
        initButtonListener()
    }

    private fun initNicknameListener() = with(binding.ofvNickname) {
        setOnTextChangeListener { nick ->
            viewModel.setNickname(nick)
        }
    }

    private fun initCheckBoxListener() {
        binding.cbTos.setOnCheckedChangeListener { _, state ->
            viewModel.setTosCheckBox(state)
            viewModel.changeCompleteState()
        }

        binding.cbPp.setOnCheckedChangeListener { _, state ->
            viewModel.setPpCheckBox(state)
            viewModel.changeCompleteState()
        }
    }

    private fun initButtonListener() {
        binding.tvPpView.setOnClickListener {
            val dialog = OmosDialog(requireActivity())
            dialog.showDialog(
                title = getString(R.string.tos_view),
                okText = getString(R.string.ok),
                cancelVisible = false,
                gravity = Gravity.NO_GRAVITY,
                sizeRatio = 1.0,
                onOkClickListener = null,
            )
        }

        binding.tvTosView.setOnClickListener {
            val dialog = OmosDialog(requireActivity())
            dialog.showDialog(
                title = getString(R.string.pp_view),
                okText = getString(R.string.ok),
                cancelVisible = false,
                gravity = Gravity.NO_GRAVITY,
                sizeRatio = 1.0,
                onOkClickListener = null
            )
        }

        binding.btnComplete.setOnClickListener {
            val directions =
                NicknameFragmentDirections.actionNicknameFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
    }

    private fun collectData() {

    }
}