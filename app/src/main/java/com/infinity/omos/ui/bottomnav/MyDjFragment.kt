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
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.MyDjListAdapter
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.databinding.FragmentMyDjBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SharedViewModel

class MyDjFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentMyDjBinding

    private val fromUserId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dj, container, false)
        activity?.let{
            viewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        val mAdapter = MyDjListAdapter(requireContext())
        binding.rvMydj.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        }

        val rAdapter = DetailCategoryListAdapter(requireContext())
        binding.rvRecord.apply {
            adapter = rAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.getMyDjRecord().observe(viewLifecycleOwner) { record ->
            record?.let {
                rAdapter.setCategory(it)
            }
        }

        viewModel.getStateDjRecord().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.rvRecord.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.lnNorecord.visibility = View.GONE
                    }

                    Constant.ApiState.DONE -> {
                        binding.rvRecord.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        }

        mAdapter.setItemClickListener(object : MyDjListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int, toUserId: Int) {
                viewModel.setMyDjRecord(fromUserId, toUserId)
            }
        })

        return binding.root
    }
}