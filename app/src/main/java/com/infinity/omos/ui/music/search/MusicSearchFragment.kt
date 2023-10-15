package com.infinity.omos.ui.music.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.infinity.omos.adapters.music.MusicTitleListAdapter
import com.infinity.omos.databinding.FragmentSearchMusicBinding
import com.infinity.omos.ui.theme.OmosTheme
import com.infinity.omos.utils.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicSearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchMusicBinding
    private val viewModel: MusicSearchViewModel by viewModels()

    private val musicTitleAdapter = MusicTitleListAdapter(::setSearchTextByTitle)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                OmosTheme {
                    MusicSearchApp()
                }
            }
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
                viewModel.setSearchState(MusicSearchState.ING)
            }
        }

        binding.etSearch.addTextChangedListener {
            viewModel.setKeyword(it.toString())
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action != KeyEvent.ACTION_DOWN)) {
                search()
            }

            false
        }
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
        viewModel.setKeyword(title)
        binding.etSearch.clearFocus()
        search()
    }

    private fun search() {
        viewModel.setSearchState(MusicSearchState.AFTER)
        viewModel.search()
    }
}