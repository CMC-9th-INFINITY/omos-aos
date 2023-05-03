package com.infinity.omos.ui.main

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
import com.infinity.omos.ui.record.DetailRecordActivity
import com.infinity.omos.ui.record.MyLikeRecordActivity
import com.infinity.omos.ui.record.MyScrapRecordActivity
import com.infinity.omos.R
import com.infinity.omos.databinding.FragmentMyPageBinding
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.ui.dj.FollowActivity
import com.infinity.omos.ui.setting.SettingActivity
import com.infinity.omos.OmosApplication

class MyPageFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentMyPageBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    private val userId = OmosApplication.prefs.getInt("userId")

    private var scrap1 = -1
    private var scrap2 = -1
    private var like1 = -1
    private var like2 = -1

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

        binding.djFollower.setOnClickListener {
            val intent = Intent(context, FollowActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("followState", 0)
            startActivity(intent)
        }

        binding.djFollowing.setOnClickListener {
            val intent = Intent(context, FollowActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("followState", 1)
            startActivity(intent)
        }

        viewModel.setDjProfile(userId, userId)
        viewModel.getDjProfile().observe(this) { profile ->
            profile?.let {
                binding.data = it
                myNickname = it.profile.nickname

                myProfileUrl = it.profile.profileUrl ?: ""
            }
        }

        viewModel.setMyPageData(userId)
        viewModel.getMyPageData().observe(this) { state ->
            state?.let {
                when(it.scrappedRecords.size){
                    2 -> {
                        binding.lnNorecordScrap.visibility = View.GONE
                        binding.lnScrap1.visibility = View.VISIBLE
                        binding.lnScrap2.visibility = View.VISIBLE
                        binding.scrap1 = it.scrappedRecords[0]
                        binding.scrap2 = it.scrappedRecords[1]
                        binding.tvArtistScrap1.text = GlobalFunction.setArtist(it.scrappedRecords[0].music.artists)
                        binding.tvArtistScrap2.text = GlobalFunction.setArtist(it.scrappedRecords[1].music.artists)

                        scrap1 = it.scrappedRecords[0].recordId
                        scrap2 = it.scrappedRecords[1].recordId
                    }

                    1 -> {
                        binding.lnNorecordScrap.visibility = View.GONE
                        binding.lnScrap1.visibility = View.VISIBLE
                        binding.lnScrap2.visibility = View.INVISIBLE
                        binding.scrap1 = it.scrappedRecords[0]
                        binding.tvArtistScrap1.text = GlobalFunction.setArtist(it.scrappedRecords[0].music.artists)

                        scrap1 = it.scrappedRecords[0].recordId
                    }

                    0 -> {
                        binding.lnNorecordScrap.visibility = View.VISIBLE
                        binding.lnScrap1.visibility = View.INVISIBLE
                        binding.lnScrap2.visibility = View.INVISIBLE
                    }
                }

                when(it.likedRecords.size){
                    2 -> {
                        binding.lnNorecordLike.visibility = View.GONE
                        binding.lnLike1.visibility = View.VISIBLE
                        binding.lnLike2.visibility = View.VISIBLE
                        binding.like1 = it.likedRecords[0]
                        binding.like2 = it.likedRecords[1]
                        binding.tvArtistLike1.text = GlobalFunction.setArtist(it.likedRecords[0].music.artists)
                        binding.tvArtistLike2.text = GlobalFunction.setArtist(it.likedRecords[1].music.artists)

                        like1 = it.likedRecords[0].recordId
                        like2 = it.likedRecords[1].recordId
                    }

                    1 -> {
                        binding.lnNorecordLike.visibility = View.GONE
                        binding.lnLike1.visibility = View.VISIBLE
                        binding.lnLike2.visibility = View.INVISIBLE
                        binding.like1 = it.likedRecords[0]
                        binding.tvArtistLike1.text = GlobalFunction.setArtist(it.likedRecords[0].music.artists)

                        like1 = it.likedRecords[0].recordId
                    }

                    0 -> {
                        binding.lnNorecordLike.visibility = View.VISIBLE
                        binding.lnLike1.visibility = View.INVISIBLE
                        binding.lnLike2.visibility = View.INVISIBLE
                    }
                }
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

        binding.lnScrap1.setOnClickListener {
            val intent = Intent(context, DetailRecordActivity::class.java)
            intent.putExtra("postId", scrap1)
            startActivity(intent)
        }

        binding.lnScrap2.setOnClickListener {
            val intent = Intent(context, DetailRecordActivity::class.java)
            intent.putExtra("postId", scrap2)
            startActivity(intent)
        }

        binding.btnAllLike.setOnClickListener {
            val intent = Intent(context, MyLikeRecordActivity::class.java)
            startActivity(intent)
        }

        binding.lnLike1.setOnClickListener {
            val intent = Intent(context, DetailRecordActivity::class.java)
            intent.putExtra("postId", like1)
            startActivity(intent)
        }

        binding.lnLike2.setOnClickListener {
            val intent = Intent(context, DetailRecordActivity::class.java)
            intent.putExtra("postId", like2)
            startActivity(intent)
        }

        // 스와이프 기능
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.setDjProfile(userId, userId)
            viewModel.setMyPageData(userId)
            binding.swipeRefreshLayout.isRefreshing = false
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
                viewModel.setMyPageData(userId)
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
                viewModel.setMyPageData(userId)
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter("SCRAP_UPDATE")
        )
    }

    companion object{
        var myNickname = ""
        var myProfileUrl = ""
    }
}