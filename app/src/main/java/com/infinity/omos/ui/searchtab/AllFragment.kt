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
import com.infinity.omos.MainActivity.Companion.keyword
import com.infinity.omos.R
import com.infinity.omos.adapters.AlbumListAdapter
import com.infinity.omos.adapters.ArtistListAdapter
import com.infinity.omos.adapters.MusicListAdapter
import com.infinity.omos.databinding.FragmentAlbumBinding
import com.infinity.omos.databinding.FragmentAllBinding
import com.infinity.omos.repository.Repository
import com.infinity.omos.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_album.*
import kotlinx.android.synthetic.main.fragment_all.*

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
            binding.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aAdapter = AlbumListAdapter(requireContext())
        binding.rvAlbum.apply{
            adapter = aAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        val mAdapter = MusicListAdapter(requireContext())
        binding.rvMusic.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        val tAdapter = ArtistListAdapter(requireContext())
        binding.rvArtist.apply {
            adapter = tAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.loadMoreMusic(keyword, 5, 0)
        viewModel.loadMoreAlbum(keyword, 5, 0)
        viewModel.loadMoreArtist(keyword, 5, 0)

        // 노래 초기화
        viewModel.music.observe(viewLifecycleOwner, Observer { music ->
            music?.let {
                if (it.isNotEmpty()){
                    mAdapter.clearRecord() // 기존 리스트 삭제
                    mAdapter.setRecord(it) // 검색된 리스트 추가
                    mAdapter.deleteLoading() // 로딩 리스트 삭제
                    mAdapter.submitList(it)
                }
            }
        })

        // 앨범 초기화
        viewModel.album.observe(viewLifecycleOwner, Observer { album ->
            album?.let {
                if (it.isNotEmpty()){
                    aAdapter.clearRecord() // 기존 리스트 삭제
                    aAdapter.setRecord(it) // 검색된 리스트 추가
                    aAdapter.deleteLoading() // 로딩 리스트 삭제
                    aAdapter.submitList(it)
                }
            }
        })

        // 아티스트 초기화
        viewModel.artist.observe(viewLifecycleOwner, Observer { artist ->
            artist?.let {
                if (it.isNotEmpty()){
                    tAdapter.clearRecord() // 기존 리스트 삭제
                    tAdapter.setRecord(it) // 검색된 리스트 추가
                    tAdapter.deleteLoading() // 로딩 리스트 삭제
                    tAdapter.submitList(it)
                }
            }
        })

        // 로딩화면
        viewModel.stateArtist.observe(viewLifecycleOwner, Observer { state ->
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

        // 검색 후 스크롤 맨 위로 이동
        mAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                scrollView.scrollTo(0, 0)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                scrollView.scrollTo(0, 0)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                scrollView.scrollTo(0, 0)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                scrollView.scrollTo(0, 0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                scrollView.scrollTo(0, 0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                scrollView.scrollTo(0, 0)
            }
        })
    }

    fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var keyword = intent?.getStringExtra("keyword")!!
                viewModel.loadMoreMusic(keyword, 5, 0)
                viewModel.loadMoreAlbum(keyword, 5, 0)
                viewModel.loadMoreArtist(keyword, 5, 0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SEARCH_UPDATE")
        )
    }
}
