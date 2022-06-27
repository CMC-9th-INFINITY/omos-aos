package com.infinity.omos.ui.dj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity.omos.R
import com.infinity.omos.adapters.FOLLOWER_PAGE_INDEX
import com.infinity.omos.adapters.FOLLOWING_PAGE_INDEX
import com.infinity.omos.adapters.FollowPagerAdapter
import com.infinity.omos.databinding.ActivityFollowBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register_nick.*

@AndroidEntryPoint
class FollowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityFollowBinding>(this, R.layout.activity_follow)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        val userId = intent.getIntExtra("userId", -1)

        initToolBar()

        viewPager.adapter = FollowPagerAdapter(this, userId)

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