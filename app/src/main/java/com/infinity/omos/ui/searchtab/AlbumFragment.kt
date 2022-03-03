package com.infinity.omos.ui.searchtab

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity.omos.MainActivity
import com.infinity.omos.MainActivity.Companion.keyword
import com.infinity.omos.R
import com.infinity.omos.adapters.AlbumListAdapter
import com.infinity.omos.databinding.FragmentAlbumBinding
import com.infinity.omos.repository.Repository
import com.infinity.omos.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_album.*

class AlbumFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentAlbumBinding

    lateinit var broadcastReceiver: BroadcastReceiver

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = AlbumListAdapter(requireContext())
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 스크롤 시 앨범 업데이트
        viewModel.loadMoreAlbum(keyword, 20, 0)
        viewModel.album.observe(viewLifecycleOwner, Observer { album ->
            album?.let {
                // 새로 검색 시 기존 리스트 삭제
                if (page == 0){
                    mAdapter.clearRecord()
                }

                mAdapter.setRecord(it)
                mAdapter.submitList(it)
            }
        })

        // 로딩화면
        viewModel.stateAlbum.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Repository.ApiState.LOADING -> {
                        if (page == 0){
                            binding.recyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }

                    Repository.ApiState.DONE -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Repository.ApiState.ERROR -> {

                    }
                }
            }
        })

        // 무한 스크롤
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    mAdapter.deleteLoading()
                    viewModel.loadMoreAlbum(keyword, 20, ++page*20)
                }
            }
        })

        // 검색 후 맨 아래로 이동하는 현상 해결
        mAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (page == 0){
                    recyclerView.scrollToPosition(0)
                }
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (page == 0){
                    recyclerView.scrollToPosition(0)
                }
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (page == 0){
                    recyclerView.scrollToPosition(0)
                }
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (page == 0){
                    recyclerView.scrollToPosition(0)
                }
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                if (page == 0){
                    recyclerView.scrollToPosition(0)
                }
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                if (page == 0){
                    recyclerView.scrollToPosition(0)
                }
            }
        })
    }

    fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var keyword = intent?.getStringExtra("keyword")!!
                page = 0
                viewModel.loadMoreAlbum(keyword, 20, 0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SEARCH_UPDATE")
        )
    }
}