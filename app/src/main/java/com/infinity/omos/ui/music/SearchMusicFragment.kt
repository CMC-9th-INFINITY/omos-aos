package com.infinity.omos.ui.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.infinity.omos.adapters.music.MusicTitleListAdapter
import com.infinity.omos.databinding.FragmentSearchMusicBinding
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMusicFragment : Fragment() {

    private lateinit var binding: FragmentSearchMusicBinding
    private val viewModel: SearchMusicViewModel by viewModels()

    private val musicTitleAdapter = MusicTitleListAdapter(::setSearchTextByTitle)

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

        setAdapter()
        initListener()
        collectData()
    }

    private fun setAdapter() {
        binding.rvKeyword.adapter = musicTitleAdapter
    }

    private fun initListener() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        binding.etSearch.setOnFocusChangeListener { _, b ->
            if (b) {
                hideTopSearched()
            }
        }

        binding.etSearch.addTextChangedListener {
            viewModel.setKeyword(it.toString())
        }
    }

    private fun hideTopSearched() {
        binding.constraintTopSearched.visibility = View.GONE
        binding.appbar.visibility = View.GONE
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.musicTitles.collect { state ->
                if (state is MusicTitleUiState.Success) {
                    musicTitleAdapter.submitList(state.titles)
                }
            }
        }
    }

    private fun setSearchTextByTitle(title: String) {
        binding.etSearch.setText(title)
        binding.etSearch.setSelection(title.length)
    }
}