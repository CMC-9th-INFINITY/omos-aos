package com.infinity.omos.ui.main.myrecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentMyRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyRecordFragment : Fragment() {

    private lateinit var binding: FragmentMyRecordBinding
    private val viewModel: MyRecordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}