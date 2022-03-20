package com.infinity.omos.ui.bottomnav

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.infinity.omos.MyLikeRecordActivity
import com.infinity.omos.MyScrapRecordActivity
import com.infinity.omos.R
import com.infinity.omos.SettingActivity
import com.infinity.omos.databinding.FragmentMyPageBinding
import com.infinity.omos.databinding.FragmentMyRecordBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.etc.Constant.Companion.NOTI_ID
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.utils.MyReceiver
import com.infinity.omos.utils.PreferenceUtil
import com.infinity.omos.viewmodels.SharedViewModel
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.fragment_my_page.*
import java.text.SimpleDateFormat
import java.util.*

class MyPageFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: FragmentMyPageBinding

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}