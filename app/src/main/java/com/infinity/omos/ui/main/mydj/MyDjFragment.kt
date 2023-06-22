package com.infinity.omos.ui.main.mydj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.infinity.omos.adapters.OmosLoadStateAdapter
import com.infinity.omos.adapters.dj.MyDjListAdapter
import com.infinity.omos.adapters.record.DetailRecordPagingAdapter
import com.infinity.omos.databinding.FragmentMyDjBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyDjFragment : Fragment() {

    private lateinit var binding: FragmentMyDjBinding
    private val viewModel: MyDjViewModel by viewModels()

    private val myDjAdapter = MyDjListAdapter(::clickItem)
    private val detailRecordAdapter = DetailRecordPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyDjBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        initListener()
        collectData()
    }

    private fun clickItem() {
        myDjAdapter.notifyItemChanged(myDjAdapter.selectedPosition.prevPosition)
        myDjAdapter.notifyItemChanged(myDjAdapter.selectedPosition.newPosition)
        viewModel.fetchDetailRecords()
    }

    private fun setAdapter() {
        binding.rvMyDjs.adapter = myDjAdapter
        binding.rvDetailRecords.adapter = detailRecordAdapter.withLoadStateFooter(
            footer = OmosLoadStateAdapter { detailRecordAdapter.retry() }
        )
    }

    private fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            detailRecordAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }

        detailRecordAdapter.addLoadStateListener { combinedLoadStates ->
            binding.progressBar.frameProgressBar.isVisible =
                combinedLoadStates.source.refresh is LoadState.Loading
        }
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.uiState.collect { state ->
                if (state is UiState.Success) {
                    myDjAdapter.submitList(state.djs)
                }
            }
        }

        repeatOnStarted {
            viewModel.detailRecords.collectLatest { records ->
                detailRecordAdapter.submitData(records)
            }
        }
    }
}