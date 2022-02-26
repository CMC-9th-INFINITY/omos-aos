package com.infinity.omos.ui.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.R
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.FragmentMyRecordBinding
import com.infinity.omos.repository.Repository
import com.infinity.omos.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_my_record.*

class MyRecordFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentMyRecordBinding

    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_record, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        val mAdapter = MyRecordListAdapter(requireContext())
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.myRecord.observe(viewLifecycleOwner, Observer { record ->
            record?.let {
                mAdapter.setRecord(it)
                mAdapter.notifyItemRangeInserted((page - 1) * 10, 10)
            }
        })

        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    //mAdapter.deleteLoading()
                    viewModel.loadMoreMyRecord(++page)
                }
            }
        })

        // 로딩화면
        viewModel.stateMyRecord.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Repository.ApiState.LOADING -> {
                        if (page == 1){
                            binding.progressBar.visibility = View.VISIBLE
                            binding.lnNorecord.visibility = View.GONE
                        }
                    }

                    Repository.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE

                        if (viewModel.myRecord.value == null){
                            binding.lnNorecord.visibility = View.VISIBLE
                        }
                    }

                    Repository.ApiState.ERROR -> {

                    }
                }
            }
        })

        return binding.root
    }
}