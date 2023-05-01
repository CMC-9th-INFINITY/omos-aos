package com.infinity.omos.ui.dj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity.omos.R
import com.infinity.omos.adapters.FOLLOWER_PAGE_INDEX
import com.infinity.omos.adapters.FOLLOWING_PAGE_INDEX
import com.infinity.omos.adapters.FollowPagerAdapter
import com.infinity.omos.databinding.ActivityFollowBinding
import com.infinity.omos.viewmodels.FollowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowBinding
    private val viewModel: FollowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_follow)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        val userId = intent.getIntExtra("userId", -1)
        val currentItemPosition = intent.getIntExtra("followState", 0)

        lifecycleScope.launch {
            viewModel.getUserProfile(userId).collectLatest {
                initToolBar(it.nickname)
            }
        }

        viewPager.adapter = FollowPagerAdapter(this, userId)
        viewPager.currentItem = currentItemPosition

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            FOLLOWER_PAGE_INDEX -> "팔로워"
            FOLLOWING_PAGE_INDEX -> "팔로잉"
            else -> null
        }
    }

    private fun initToolBar(name: String){
        binding.toolbar.title = name
        setSupportActionBar(binding.toolbar) // 툴바 사용

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