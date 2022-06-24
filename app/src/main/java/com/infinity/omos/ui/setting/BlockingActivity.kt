package com.infinity.omos.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.infinity.omos.R
import com.infinity.omos.adapters.BlockedListAdapter
import com.infinity.omos.databinding.ActivityBlockingBinding
import com.infinity.omos.databinding.ActivityMyScrapRecordBinding
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.BlockedViewModel
import com.infinity.omos.viewmodels.MyScrapRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register_nick.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlockingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockingBinding
    private val adapter = BlockedListAdapter(this)
    private val userId = GlobalApplication.prefs.getInt("userId")
    private val viewModel: BlockedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocking)
        initToolBar()

        binding.recyclerView.adapter = adapter
        search(userId)
    }

    private fun search(userId: Int) {
        lifecycleScope.launch {
            viewModel.getBlockedUsers(userId).collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun initToolBar(){
        toolbar.title = "차단된 계정"
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