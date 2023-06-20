package com.infinity.omos.ui.main.mydj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infinity.omos.adapters.dj.MyDjListAdapter
import com.infinity.omos.databinding.FragmentMyDjBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDjFragment : Fragment() {

    private lateinit var binding: FragmentMyDjBinding
    private val viewModel: MyDjViewModel by viewModels()

    private val myDjAdapter = MyDjListAdapter(::clickItem)

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
                if (state is UiState.Success) {
                    myDjAdapter.submitList(state.djs)
                }
            }
        }
    }
}