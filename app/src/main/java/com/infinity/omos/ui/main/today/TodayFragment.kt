package com.infinity.omos.ui.main.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.infinity.omos.BuildConfig
import com.infinity.omos.adapters.dj.RecommendedDjListAdapter
import com.infinity.omos.adapters.record.HorizontalRecordListAdapter
import com.infinity.omos.databinding.FragmentTodayBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private lateinit var binding: FragmentTodayBinding
    private val viewModel: TodayViewModel by viewModels()

    private val horizontalRecordAdapter = HorizontalRecordListAdapter()
    private val recommendedDjAdapter = RecommendedDjListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setMainImage()
        initListener()
        collectData()
    }

    private fun setAdapter() {
        binding.rvFamousRecords.adapter = horizontalRecordAdapter
        binding.rvDj.adapter = recommendedDjAdapter
    }

    private fun initListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.btnFloating.setOnClickListener {
            // TODO: 노래 선택 페이지 이동
        }
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.todayMusicUiState.collect { state ->
                if (state is TodayMusicUiState.Success) {
                    binding.todayMusic = state.todayMusic
                }
            }
        }

        repeatOnStarted {
            viewModel.famousRecordsUiState.collect { state ->
                if (state is FamousRecordsUiState.Success) {
                    horizontalRecordAdapter.submitList(state.famousRecords)
                }
            }
        }

        repeatOnStarted {
            viewModel.recommendedDjsUiState.collect { state ->
                if (state is RecommendedDjsUiState.Success) {
                    recommendedDjAdapter.submitList(state.recommendedDjs)
                }
            }
        }

        repeatOnStarted {
            viewModel.lovedMusicUiState.collect { state ->
                if (state is LovedMusicUiState.Success) {
                    binding.lovedMusic = state.lovedMusic
                }
            }
        }
    }

    private fun setMainImage() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)

        Glide.with(binding.ivMain.context)
            .load(BuildConfig.S3_BASE_URL + "main/$day.png")
            .into(binding.ivMain)
    }
}