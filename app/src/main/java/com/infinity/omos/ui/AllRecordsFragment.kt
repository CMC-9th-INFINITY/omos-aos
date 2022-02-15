package com.infinity.omos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
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

        val mAdapter = AllRecordsListAdapter(context!!)
        mAdapter.setCategory(addCategory())
        binding.rvAllrecords.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        return binding.root
    }

    private fun addCategory(): List<AllRecords> {
        return listOf(
            AllRecords(resources.getString(R.string.category1), listOf(MyRecord("one", "hi", "hi", "hi"))),
            AllRecords(resources.getString(R.string.category2), listOf(MyRecord("two", "hi", "hi", "hi"))),
            AllRecords(resources.getString(R.string.category3), listOf(MyRecord("three", "hi", "hi", "hi"))),
            AllRecords(resources.getString(R.string.category4), listOf(MyRecord("four", "hi", "hi", "hi"))),
            AllRecords(resources.getString(R.string.category5), listOf(MyRecord("five", "hi", "hi", "hi")))
        )
    }
}