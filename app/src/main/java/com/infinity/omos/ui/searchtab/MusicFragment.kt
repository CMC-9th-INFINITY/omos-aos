package com.infinity.omos.ui.searchtab

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.adapters.MusicListAdapter
import com.infinity.omos.databinding.FragmentMusicBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.Height.Companion.navigationHeight
import com.infinity.omos.viewmodels.MainViewModel

class MusicFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMusicBinding

    lateinit var broadcastReceiver: BroadcastReceiver

    private var page = 0
    private val pageSize = 20
    private var isLoading = false

    private lateinit var mAdapter: MusicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

        // 밑에 짤리는 현상 해결
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.recyclerView.setPadding(0, 0, 0, context!!.navigationHeight())
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = MusicListAdapter(requireContext())
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 스크롤 시 앨범 업데이트
        viewModel.loadMoreMusic(MainActivity.keyword, pageSize, 0)
        viewModel.getMusic().observe(viewLifecycleOwner) { music ->
            music?.let {
                isLoading = if (it.isEmpty()) {
                    mAdapter.notifyItemRemoved(mAdapter.itemCount)
                    true
                } else {
                    mAdapter.setRecord(it)
                    mAdapter.notifyItemRangeInserted(page * pageSize, it.size)
                    false
                }

                // 작성한 레코드가 없을 때,
                if (it.isEmpty() && page == 0) {
                    binding.lnNorecord.visibility = View.VISIBLE
                }
            }
        }

        // 로딩화면
        viewModel.getStateMusic().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        if (page == 0) {
                            binding.recyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                            binding.lnNorecord.visibility = View.GONE
                        }
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

        // 무한 스크롤
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading && itemTotalCount > -1) {
                    isLoading = true
                    mAdapter.deleteLoading()
                    viewModel.loadMoreMusic(MainActivity.keyword, pageSize, ++page*pageSize)
                }
            }
        })
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                page = 0
                mAdapter.clearRecord()
                binding.recyclerView.scrollToPosition(0)
                var keyword = intent?.getStringExtra("keyword")!!
                viewModel.loadMoreMusic(keyword, pageSize, 0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SEARCH_UPDATE")
        )
    }
}