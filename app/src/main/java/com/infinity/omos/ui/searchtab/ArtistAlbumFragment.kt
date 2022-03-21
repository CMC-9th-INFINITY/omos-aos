package com.infinity.omos.ui.searchtab

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
import com.infinity.omos.R
import com.infinity.omos.adapters.AlbumListAdapter
import com.infinity.omos.databinding.FragmentArtistAlbumBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.viewmodels.ArtistViewModel

class ArtistAlbumFragment : Fragment() {

    private val viewModel: ArtistViewModel by viewModels()
    private lateinit var binding: FragmentArtistAlbumBinding

    private var page = 0
    private val pageSize = 20
    private var isLoading = false

    private var artistId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_album, container, false)
        activity?.let{
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

        var bundle = this.arguments
        artistId = bundle?.getString("artistId")!!

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
        viewModel.setArtistAlbum(artistId, pageSize, 0)
        viewModel.getArtistAlbum().observe(viewLifecycleOwner, Observer { album ->
            album?.let {
                isLoading = if (it.isEmpty()) {
                    mAdapter.notifyItemRemoved(mAdapter.itemCount)
                    true
                } else {
                    mAdapter.setRecord(it)
                    mAdapter.notifyItemRangeInserted(page * pageSize, it.size)
                    false
                }
            }
        })

        // 로딩화면
        viewModel.getStateArtistAlbum().observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        if (page == 0){
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
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !isLoading) {
                    isLoading = true
                    mAdapter.deleteLoading()
                    viewModel.setArtistAlbum(artistId, pageSize, ++page*pageSize)
                }
            }
        })
    }
}