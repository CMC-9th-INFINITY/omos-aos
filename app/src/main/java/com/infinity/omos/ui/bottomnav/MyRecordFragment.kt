package com.infinity.omos.ui.bottomnav

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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.FragmentMyRecordBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SharedViewModel

class MyRecordFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentMyRecordBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_record, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = MyRecordListAdapter(requireContext())
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 레코드 셋팅
        viewModel.setMyRecord(userId)
        viewModel.getMyRecord().observe(viewLifecycleOwner) { record ->
            record?.let {
                mAdapter.setRecord(it)

                // 작성한 레코드가 없을 때,
                if (it.isEmpty()) {
                    binding.lnNorecord.visibility = View.VISIBLE
                }
            }
        }

        // 로딩화면
        viewModel.getStateMyRecord().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.lnNorecord.visibility = View.GONE
                    }

                    Constant.ApiState.DONE -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                    else -> {}
                }
            }
        }

        // 검색
        (activity as MainActivity).setFilterListener(object : MainActivity.OnFilterListener{
            override fun onFilter(str: String) {
                mAdapter.filter.filter(str)
            }
        })
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.setMyRecord(userId)
                binding.recyclerView.scrollToPosition(0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("RECORD_UPDATE")
        )
    }
}