package com.infinity.omos.ui.main.allrecords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.infinity.omos.adapters.record.CategoryListAdapter
import com.infinity.omos.databinding.FragmentAllRecordsBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllRecordsFragment : Fragment() {

    private lateinit var binding: FragmentAllRecordsBinding
    private val viewModel: AllRecordsViewModel by viewModels()

    private val adapter = CategoryListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllRecordsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCategory.adapter = adapter

        initListener()
        collectData()
    }

    private fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                binding.sflCategory.isVisible = state is UiState.Loading
                if (state is UiState.Success) {
                    binding.sflCategory.stopShimmer()
                    adapter.submitList(state.categories)
                }
            }
        }
    }
}