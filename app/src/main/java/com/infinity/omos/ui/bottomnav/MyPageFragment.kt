package com.infinity.omos.ui.bottomnav

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.infinity.omos.MyLikeRecordActivity
import com.infinity.omos.MyScrapRecordActivity
import com.infinity.omos.R
import com.infinity.omos.ui.setting.SettingActivity
import com.infinity.omos.databinding.FragmentMyPageBinding
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_my_page.*
import java.util.*

class MyPageFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentMyPageBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBroadcastReceiver()
        likeBroadcastReceiver()
        scrapBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        activity?.let{
            binding.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDjProfile(userId, userId)
        viewModel.getDjProfile().observe(this) { profile ->
            profile?.let {
                binding.data = it
            }
        }

        binding.btnSetting.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.btnAllScrap.setOnClickListener {
            val intent = Intent(context, MyScrapRecordActivity::class.java)
            startActivity(intent)
        }

        binding.btnAllLike.setOnClickListener {
            val intent = Intent(context, MyLikeRecordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.setDjProfile(userId, userId)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("PROFILE_UPDATE")
        )
    }

    private fun likeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("LIKE_UPDATE")
        )
    }

    private fun scrapBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SCRAP_UPDATE")
        )
    }
}