package com.infinity.omos.ui.searchtab

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentArtistMusicBinding
import com.infinity.omos.databinding.FragmentMusicBinding
import com.infinity.omos.viewmodels.ArtistViewModel
import com.infinity.omos.viewmodels.MainViewModel

class ArtistMusicFragment : Fragment() {

    private val viewModel: ArtistViewModel by viewModels()
    private lateinit var binding: FragmentArtistMusicBinding

    private var page = 0

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}