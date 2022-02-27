package com.infinity.omos.ui.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.R
import com.infinity.omos.adapters.AllRecordsListAdapter
import com.infinity.omos.data.AllRecords
import com.infinity.omos.data.Category
import com.infinity.omos.data.MyRecord
import com.infinity.omos.databinding.FragmentAllRecordsBinding
import com.infinity.omos.repository.Repository
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
        binding.rvAllrecords.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 카테고리 리스트 초기화
        viewModel.setAllRecords()
        viewModel.allRecords.observe(viewLifecycleOwner, Observer { records ->
            records?.let {
                mAdapter.setCategory(addCategory(it))
            }
        })

        // 빈 카테고리 및 토큰 만료 상태 확인
        viewModel.stateAllRecords.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Repository.ApiState.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Repository.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    Repository.ApiState.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        viewModel.setAllRecords()
                    }
                }
            }
        })

        return binding.root
    }

    private fun addCategory(category: Category): List<AllRecords> {
        return listOf(
            AllRecords(resources.getString(R.string.category1), category.a_line),
            AllRecords(resources.getString(R.string.category2), category.ost),
            AllRecords(resources.getString(R.string.category3), category.story),
            AllRecords(resources.getString(R.string.category4), category.lyrics),
            AllRecords(resources.getString(R.string.category5), category.free)
        )
    }
}