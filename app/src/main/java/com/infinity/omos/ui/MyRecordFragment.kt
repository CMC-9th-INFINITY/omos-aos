package com.infinity.omos.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.R
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.FragmentMyRecordBinding
import com.infinity.omos.viewmodels.SharedViewModel

class MyRecordFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentMyRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_record, container, false)
        activity?.let{
            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        val mAdapter = MyRecordListAdapter(requireContext())
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 레코드가 비었을 때 확인,
        viewModel.isEmpty.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                if (it){
                    binding.lnNorecord.visibility = View.VISIBLE
                } else{
                    binding.lnNorecord.visibility = View.GONE
                }
            }
        })

        return binding.root
    }
}