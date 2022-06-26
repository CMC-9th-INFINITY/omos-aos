package com.infinity.omos.ui.searchtab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.R
import com.infinity.omos.adapters.ArtistMusicListAdapter
import com.infinity.omos.databinding.FragmentArtistMusicBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.viewmodels.ArtistViewModel

class ArtistMusicFragment : Fragment() {

    private val viewModel: ArtistViewModel by viewModels()
    private lateinit var binding: FragmentArtistMusicBinding

    private var page = 0

    private var artistId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_music, container, false)
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

        val mAdapter = ArtistMusicListAdapter(requireContext())
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.setArtistMusic(artistId)
        viewModel.getArtistMusic().observe(viewLifecycleOwner, Observer { music ->
            music?.let {
                mAdapter.setRecord(it)
            }
        })

        // 로딩화면
        viewModel.getStateArtistMusic().observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
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
        })
    }
}