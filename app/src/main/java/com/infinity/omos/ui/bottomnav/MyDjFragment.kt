package com.infinity.omos.ui.bottomnav

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.adapters.DetailCategoryListAdapter
import com.infinity.omos.adapters.MyDjListAdapter
import com.infinity.omos.data.SaveRecord
import com.infinity.omos.databinding.FragmentMyDjBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_artist.*

class MyDjFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentMyDjBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    private val fromUserId = GlobalApplication.prefs.getInt("userId")

    private var page = 0
    private val pageSize = 5
    private var isLoading = false
    private var isFirst = true

    private var postId = -1
    private var tmpUserId = -1
    private lateinit var mAdapter: MyDjListAdapter
    private lateinit var rAdapter: DetailCategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
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

        mAdapter = MyDjListAdapter(requireContext())
        binding.rvMydj.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity).also { it.orientation = LinearLayoutManager.HORIZONTAL }
        }

        rAdapter = DetailCategoryListAdapter(requireContext())
        binding.rvRecord.apply {
            adapter = rAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.setMyDj(fromUserId)
        viewModel.getMyDj().observe(viewLifecycleOwner) { dj ->
            dj?.let {
                mAdapter.setDj(it)

                if (it.isEmpty()){
                    binding.linear.visibility = View.GONE
                    binding.lnNodj.visibility = View.VISIBLE
                } else {
                    binding.lnNodj.visibility = View.GONE
                }
            }
        }

        viewModel.getStateMyDj().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.linear.visibility = View.GONE
                        binding.lnNodj.visibility = View.GONE
                    }

                    Constant.ApiState.DONE -> {
                        binding.linear.visibility = View.VISIBLE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        }

        viewModel.setDjAllRecords(fromUserId, null, pageSize)
        viewModel.getDjAllRecords().observe(viewLifecycleOwner) { record ->
            record?.let {
                isLoading = if (it.isEmpty()) {
                    rAdapter.notifyItemRemoved(rAdapter.itemCount)
                    true
                } else {
                    rAdapter.addCategory(it)
                    rAdapter.notifyItemRangeInserted(page * pageSize, it.size)
                    postId = it[it.lastIndex].recordId
                    false
                }
            }
        }

        viewModel.getStateDjAllRecords().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        if (page == 0){
                            binding.rvRecord.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                            binding.lnNorecord.visibility = View.GONE
                        }
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
                // 한 번 더 클릭 시 첫 화면 노출
                if (position == -1){
                    setFirst()
                } else {
                    isFirst = false
                    tmpUserId = toUserId
                    viewModel.setMyDjRecord(fromUserId, toUserId)
                }
                binding.rvRecord.scrollToPosition(0)
            }
        })

        // 무한 스크롤
        binding.rvRecord.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.rvRecord.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading && isFirst) {
                    isLoading = true
                    page ++
                    rAdapter.deleteLoading()
                    viewModel.setDjAllRecords(fromUserId, postId, pageSize)
                }
            }
        })

        // 스와이프 기능
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (tmpUserId == -1){
                setFirst()
            } else {
                isFirst = false
                viewModel.setMyDjRecord(fromUserId, tmpUserId)
            }
            binding.rvRecord.scrollToPosition(0)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root
    }

    private fun setFirst(){
        page = 0
        isLoading = false
        isFirst = true
        tmpUserId = -1
        rAdapter.clearCategory()
        viewModel.setDjAllRecords(fromUserId, null, pageSize)
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                setFirst()
                viewModel.setMyDj(fromUserId)
                binding.rvRecord.scrollToPosition(0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("DJ_UPDATE")
        )
    }
}