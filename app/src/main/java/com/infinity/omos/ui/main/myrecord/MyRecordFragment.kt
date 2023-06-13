package com.infinity.omos.ui.main.myrecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.infinity.omos.R
import com.infinity.omos.adapters.record.VerticalRecordListAdapter
import com.infinity.omos.databinding.FragmentMyRecordBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyRecordFragment : Fragment() {

    private lateinit var binding: FragmentMyRecordBinding
    private val viewModel: MyRecordViewModel by viewModels()

    private val adapter = VerticalRecordListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMyRecord.adapter = adapter

        collectData()
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                binding.sflMyRecord.isVisible = state is UiState.Loading
                if (state is UiState.Success) {
                    binding.sflMyRecord.stopShimmer()
                    adapter.submitList(state.records)
                }
            }
        }
    }
}