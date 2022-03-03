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
import com.infinity.omos.MainActivity.Companion.keyword
import com.infinity.omos.R
import com.infinity.omos.adapters.AlbumListAdapter
import com.infinity.omos.databinding.FragmentAlbumBinding
import com.infinity.omos.databinding.FragmentAllBinding
import com.infinity.omos.repository.Repository
import com.infinity.omos.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class AllFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentAllBinding

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = this
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = AlbumListAdapter(requireContext())
        binding.rvAlbum.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // 스크롤 시 앨범 업데이트
        viewModel.loadMoreAlbum(keyword, 5, 0)
        viewModel.album.observe(viewLifecycleOwner, Observer { album ->
            album?.let {
                mAdapter.clearRecord() // 기존 리스트 삭제
                mAdapter.setRecord(it) // 검색된 리스트 추가
                mAdapter.deleteLoading() // 로딩 리스트 삭제
                mAdapter.submitList(it)
            }
        })

        // 로딩화면
        viewModel.stateAlbum.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Repository.ApiState.LOADING -> {
                        binding.scrollView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Repository.ApiState.DONE -> {
                        binding.scrollView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Repository.ApiState.ERROR -> {

                    }
                }
            }
        })
    }

    fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var keyword = intent?.getStringExtra("keyword")!!
                viewModel.loadMoreAlbum(keyword, 5, 0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SEARCH_UPDATE")
        )
    }
}
