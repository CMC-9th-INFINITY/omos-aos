package com.infinity.omos.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.R
import com.infinity.omos.adapters.AllRecordsListAdapter
import com.infinity.omos.data.FakeAllRecords
import com.infinity.omos.data.Category
import com.infinity.omos.databinding.FragmentFakeAllRecordsBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.OmosApplication

class AllRecordFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentFakeAllRecordsBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    private val userId = OmosApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentFakeAllRecordsBinding?>(inflater,
            R.layout.fragment_fake_all_records,
            container,
            false
        ).apply {
            activity?.let {
                viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
                vm = viewModel
                lifecycleOwner = this@AllRecordFragment
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = AllRecordsListAdapter(requireContext())
        binding.rvAllrecords.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 카테고리 리스트 초기화
        viewModel.setAllRecords(userId)
        viewModel.getAllRecords().observe(viewLifecycleOwner, Observer { records ->
            records?.let {
                mAdapter.setCategory(addCategory(it))
            }
        })

        // 빈 카테고리 및 토큰 만료 상태 확인
        viewModel.getStateAllRecords().observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        })

        // 스와이프 기능
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.setAllRecords(userId)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun addCategory(category: Category): List<FakeAllRecords> {
        return listOf(
            FakeAllRecords(resources.getString(R.string.a_line), category.a_line),
            FakeAllRecords(resources.getString(R.string.ost), category.ost),
            FakeAllRecords(resources.getString(R.string.story), category.story),
            FakeAllRecords(resources.getString(R.string.lyrics), category.lyrics),
            FakeAllRecords(resources.getString(R.string.free), category.free)
        )
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.setAllRecords(userId)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("RECORD_UPDATE")
        )
    }
}