package com.infinity.omos.ui.main.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infinity.omos.adapters.record.HorizontalRecordListAdapter
import com.infinity.omos.databinding.FragmentMyPageBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private val viewModel: MyPageViewModel by viewModels()

    private val scrapedAdapter = HorizontalRecordListAdapter(onClick = {})
    private val likedAdapter = HorizontalRecordListAdapter(onClick = {})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPageBinding.inflate(inflater, container, false).apply {
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

    private fun setAdapter() {
        binding.layoutScrapedRecords.rvPreviewRecords.adapter = scrapedAdapter
        binding.layoutLikedRecords.rvPreviewRecords.adapter = likedAdapter
    }

    private fun initListener() {

    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.profile.collect { state ->
                if (state is ProfileUiState.Success) {
                    binding.profile = state.profile
                }
            }
        }

        repeatOnStarted {
            viewModel.myPageRecords.collect { state ->
                if (state is MyPageRecordUiState.Success) {
                    scrapedAdapter.submitList(state.myPageRecords.scrappedRecords)
                    likedAdapter.submitList(state.myPageRecords.likedRecords)
                }
            }
        }
    }
}