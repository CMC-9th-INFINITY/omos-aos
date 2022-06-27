package com.infinity.omos.ui.dj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.infinity.omos.R
import com.infinity.omos.adapters.FollowerListAdapter
import com.infinity.omos.databinding.FragmentFollowerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFollowerBinding.inflate(inflater, container, false)

        val adapter = FollowerListAdapter()
        binding.followerList.adapter = adapter

        return binding.root
    }
}