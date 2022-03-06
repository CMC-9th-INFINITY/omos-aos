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
import com.infinity.omos.ArtistActivity
import com.infinity.omos.MainActivity
import com.infinity.omos.R
import com.infinity.omos.adapters.ArtistListAdapter
import com.infinity.omos.data.Artists
import com.infinity.omos.databinding.FragmentArtistBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.repository.Repository
import com.infinity.omos.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_artist.*

class ArtistFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentArtistBinding

    lateinit var broadcastReceiver: BroadcastReceiver

    private var page = 0
    private val pageSize = 20
    private var isLoading = false

    private lateinit var mAdapter: ArtistListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false)
        activity?.let {
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ArtistListAdapter(requireContext())
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        mAdapter.setItemClickListener(object : ArtistListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int, artist: Artists, tvGenres: String) {
                val intent = Intent(context, ArtistActivity::class.java)
                intent.putExtra("artistName", artist.artistName)
                intent.putExtra("artistImageUrl", artist.artistImageUrl)
                intent.putExtra("artistGenres", tvGenres)
                startActivity(intent)
            }
        })

        // 스크롤 시 앨범 업데이트
        viewModel.loadMoreArtist(MainActivity.keyword, pageSize, 0)
        viewModel.artist.observe(viewLifecycleOwner, Observer { artist ->
            artist?.let {
                mAdapter.setRecord(it)
                isLoading = if (it.isEmpty()) {
                    mAdapter.deleteLoading()
                    mAdapter.notifyItemRemoved(mAdapter.itemCount-1)
                    true
                } else {
                    mAdapter.submitList(it)
                    false
                }
            }
        })

        // 로딩화면
        viewModel.stateArtist.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        if (page == 0) {
                            binding.recyclerView.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE

                        }
                    }

                    Constant.ApiState.DONE -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        })

        // 무한 스크롤
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading) {
                    isLoading = true
                    mAdapter.deleteLoading()
                    viewModel.loadMoreArtist(MainActivity.keyword, pageSize, ++page * pageSize)
                    Log.d("testScroll", isLoading.toString())
                }
            }
        })

        // 검색 후 맨 아래로 이동하는 현상 해결
        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (page == 0) {
                    recyclerView.scrollToPosition(0)
                }
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (page == 0) {
                    recyclerView.scrollToPosition(0)
                }
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (page == 0) {
                    recyclerView.scrollToPosition(0)
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (page == 0) {
                    recyclerView.scrollToPosition(0)
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                if (page == 0) {
                    recyclerView.scrollToPosition(0)
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                if (page == 0) {
                    recyclerView.scrollToPosition(0)
                }
            }
        })
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                page = 0
                mAdapter = ArtistListAdapter(requireContext())
                binding.recyclerView.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(activity)
                }

                var keyword = intent?.getStringExtra("keyword")!!
                viewModel.loadMoreArtist(keyword, pageSize, 0)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SEARCH_UPDATE")
        )
    }
}