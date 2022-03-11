package com.infinity.omos

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

    private val fromUserId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityDjBinding>(this, R.layout.activity_dj)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val toUserId = intent.getIntExtra("toUserId", -1)

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
                    viewModel.follow.value = "팔로잉"
                } else{
                    viewModel.follow.value = "팔로우"
                }
            }
        }

        viewModel.setDjRecord(fromUserId, toUserId)
        viewModel.getDjRecord().observe(this) { record ->
            record?.let {
                mAdapter.setRecord(it)

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

        viewModel.follow.observe(this) { state ->
            state?.let {
                if (it == "팔로우") {
                    binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.btnFollow.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.bg_follow, null)
                    viewModel.deleteFollow(fromUserId, toUserId)
                } else {
                    binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.gray_04))
                    binding.btnFollow.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.bg_following, null)
                    viewModel.saveFollow(fromUserId, toUserId)
                }
            }
        }
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
}