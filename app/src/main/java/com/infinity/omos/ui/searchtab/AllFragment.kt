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
import com.infinity.omos.MainActivity.Companion.keyword
import com.infinity.omos.R
import com.infinity.omos.adapters.AlbumListAdapter
import com.infinity.omos.adapters.ArtistListAdapter
import com.infinity.omos.adapters.MusicListAdapter
import com.infinity.omos.databinding.FragmentAllBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.Height.Companion.navigationHeight
import com.infinity.omos.viewmodels.MainViewModel

class AllFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentAllBinding

    lateinit var broadcastReceiver: BroadcastReceiver

    private var isEmpty = 0

    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun setCurrentItem(position: Int)
    }

    fun setCurrentItem(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

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

        // 풀스크린 시 밑에 짤리는 현상 해결
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.scrollView.setPadding(0, 0, 0, context!!.navigationHeight())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAdapter = MusicListAdapter(requireContext())
        binding.rvMusic.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        val aAdapter = AlbumListAdapter(requireContext())
        binding.rvAlbum.apply{
            adapter = aAdapter
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
        viewModel.getMusic().observe(viewLifecycleOwner) { music ->
            music?.let {
                mAdapter.clearRecord() // 기존 리스트 삭제
                mAdapter.setRecord(it) // 검색된 리스트 추가
                mAdapter.deleteLoading() // 로딩 리스트 삭제
                mAdapter.notifyDataSetChanged()

                if (it.isEmpty()) {
                    isEmpty++
                    if (isEmpty == 3) {
                        binding.lnNorecord.visibility = View.VISIBLE
                        binding.scrollView.visibility = View.GONE
                    }
                } else {
                    binding.scrollView.visibility = View.VISIBLE
                }
            }
        }

        // 앨범 초기화
        viewModel.getAlbum().observe(viewLifecycleOwner) { album ->
            album?.let {
                aAdapter.clearRecord() // 기존 리스트 삭제
                aAdapter.setRecord(it) // 검색된 리스트 추가
                aAdapter.deleteLoading() // 로딩 리스트 삭제
                aAdapter.notifyDataSetChanged()

                if (it.isEmpty()) {
                    isEmpty++
                    if (isEmpty == 3) {
                        binding.lnNorecord.visibility = View.VISIBLE
                        binding.scrollView.visibility = View.GONE
                    }
                } else {
                    binding.scrollView.visibility = View.VISIBLE
                }
            }
        }

        // 아티스트 초기화
        viewModel.getArtist().observe(viewLifecycleOwner) { artist ->
            artist?.let {
                tAdapter.clearRecord() // 기존 리스트 삭제
                tAdapter.setRecord(it) // 검색된 리스트 추가
                tAdapter.deleteLoading() // 로딩 리스트 삭제
                tAdapter.notifyDataSetChanged()

                if (it.isEmpty()) {
                    isEmpty++
                    if (isEmpty == 3) {
                        binding.lnNorecord.visibility = View.VISIBLE
                        binding.scrollView.visibility = View.GONE
                    }
                } else {
                    binding.scrollView.visibility = View.VISIBLE
                }
            }
        }

        // 로딩화면
        viewModel.getStateArtist().observe(viewLifecycleOwner) { state ->
            state?.let {
                when (it) {
                    Constant.ApiState.LOADING -> {
                        binding.scrollView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.lnNorecord.visibility = View.GONE
                    }

                    Constant.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        }

        // 더보기 클릭 시 이동
        binding.lnMusicMore.setOnClickListener {
            itemClickListener.setCurrentItem(1)
        }

        binding.lnAlbumMore.setOnClickListener {
            itemClickListener.setCurrentItem(2)
        }

        binding.lnArtistMore.setOnClickListener {
            itemClickListener.setCurrentItem(3)
        }
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var keyword = intent?.getStringExtra("keyword")!!
                isEmpty = 0
                binding.scrollView.scrollTo(0, 0)
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
