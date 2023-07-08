package com.infinity.omos.ui.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.infinity.omos.databinding.FragmentSearchMusicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMusicFragment : Fragment() {

    private lateinit var binding: FragmentSearchMusicBinding
    private val viewModel: SearchMusicViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
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

        binding.etSearch.addTextChangedListener {
            viewModel.keyword = it.toString()
            viewModel.changeMusicKeywordVisibility()
            viewModel.fetchSearchMusic()
        }
    }
}