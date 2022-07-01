package com.infinity.omos.ui.dj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.infinity.omos.adapters.FollowerListAdapter
import com.infinity.omos.databinding.FragmentFollowerBinding
import com.infinity.omos.viewmodels.FollowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerFragment(private val userId: Int) : Fragment(){

    private val viewModel: FollowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFollowerBinding.inflate(inflater, container, false)

        val adapter = FollowerListAdapter()
        binding.followerList.adapter = adapter
        lifecycleScope.launch {
            subscribeUi(adapter)
        }
        return binding.root
    }

    private suspend fun subscribeUi(adapter: FollowerListAdapter) {
        viewModel.getFollower(userId).collectLatest {
            adapter.submitList(it)
        }
    }
}