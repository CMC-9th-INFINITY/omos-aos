package com.infinity.omos.ui.bottomnav

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
import com.infinity.omos.data.AllRecords
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.FragmentAllRecordsBinding
import com.infinity.omos.viewmodels.SharedViewModel

class AllRecordFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentAllRecordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_records, container, false)
        activity?.let {
            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        val mAdapter = AllRecordsListAdapter(requireContext())
        mAdapter.setCategory(addCategory())
        binding.rvAllrecords.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 한 줄 감상 데이터 관찰
        viewModel.oneLineRecord.observe(viewLifecycleOwner, Observer { record ->
            record?.let {
                oneLineRecord = it // Category Activity 에서 사용하기 위함
                mAdapter.updateCategory(it, 0)
            }
        })

        // 내 인생의 OST 데이터 관찰
        viewModel.myOstRecord.observe(viewLifecycleOwner, Observer { record ->
            record?.let {
                myOstRecord = it
                mAdapter.updateCategory(it, 1)
            }
        })

        // 노래 속 나의 이야기 데이터 관찰
        viewModel.myStoryRecord.observe(viewLifecycleOwner, Observer { record ->
            record?.let {
                myStoryRecord = it
                mAdapter.updateCategory(it, 2)
            }
        })

        // 나만의 가사해석 데이터 관찰
        viewModel.interpretRecord.observe(viewLifecycleOwner, Observer { record ->
            record?.let {
                interpretRecord = it
                mAdapter.updateCategory(it, 3)
            }
        })

        // 자유 공간 데이터 관찰
        viewModel.freeRecord.observe(viewLifecycleOwner, Observer { record ->
            record?.let {
                freeRecord = it
                mAdapter.updateCategory(it, 4)
            }
        })

        return binding.root
    }

    private fun addCategory(): List<AllRecords> {
        return listOf(
            AllRecords(resources.getString(R.string.category1), null),
            AllRecords(resources.getString(R.string.category2), null),
            AllRecords(resources.getString(R.string.category3), null),
            AllRecords(resources.getString(R.string.category4), null),
            AllRecords(resources.getString(R.string.category5), null)
        )
    }

    companion object{
        var oneLineRecord = emptyList<MyRecord>()
        var myOstRecord = emptyList<MyRecord>()
        var myStoryRecord = emptyList<MyRecord>()
        var interpretRecord = emptyList<MyRecord>()
        var freeRecord = emptyList<MyRecord>()
    }
}