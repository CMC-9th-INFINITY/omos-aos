package com.infinity.omos

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.ActivityDjBinding
import com.infinity.omos.databinding.ActivitySelectCategoryBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.ui.onboarding.LoginActivity
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.DjViewModel
import kotlinx.android.synthetic.main.activity_dj.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.toolbar

class DjActivity : AppCompatActivity() {

    private val viewModel: DjViewModel by viewModels()
    private lateinit var binding: ActivityDjBinding

    private val fromUserId = GlobalApplication.prefs.getInt("userId")
    private var toUserId = -1
    private var isClickFollow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dj)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        toUserId = intent.getIntExtra("toUserId", -1)

        // 내 프로필이라면,
        if (fromUserId == toUserId){
            binding.btnFollow.visibility = View.GONE
        }

        initToolBar()

        val mAdapter = MyRecordListAdapter(this)
        binding.rvRecord.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.setDjProfile(fromUserId, toUserId)
        viewModel.getDjProfile().observe(this) { profile ->
            profile?.let {
                binding.data = it
                if (it.isFollowed){
                    setFollowing()
                } else{
                    setFollow()
                }
            }
        }

        viewModel.setDjRecord(fromUserId, toUserId)
        viewModel.getDjRecord().observe(this) { record ->
            record?.let {
                mAdapter.setDjRecord(it)

                if (it.isEmpty()){
                    binding.lnNorecord.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getStateDjRecord().observe(this) { state ->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.lnNorecord.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        }

        binding.btnFollow.setOnClickListener {
            if (binding.btnFollow.text == "팔로우"){
                setFollowing()
                viewModel.saveFollow(fromUserId, toUserId)
            } else{
                setFollow()
                viewModel.deleteFollow(fromUserId, toUserId)
            }
            isClickFollow = true
        }
    }

    private fun setFollow(){
        binding.btnFollow.text = "팔로우"
        binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.btnFollow.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_follow, null)
    }

    private fun setFollowing(){
        binding.btnFollow.text = "팔로잉"
        binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.gray_04))
        binding.btnFollow.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_following, null)
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isClickFollow){
            val intent = Intent("DJ_UPDATE")
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
            sendBroadcast(intent)
        }
    }
}