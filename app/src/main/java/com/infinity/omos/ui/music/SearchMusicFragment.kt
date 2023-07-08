package com.infinity.omos.ui.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.infinity.omos.databinding.FragmentSearchMusicBinding

class SearchMusicFragment : Fragment() {

    private lateinit var binding: FragmentSearchMusicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }
}