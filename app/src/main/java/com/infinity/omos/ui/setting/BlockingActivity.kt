package com.infinity.omos.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.infinity.omos.R
import com.infinity.omos.adapters.BlockedListAdapter
import com.infinity.omos.databinding.ActivityBlockingBinding
import com.infinity.omos.databinding.ActivityMyScrapRecordBinding
import com.infinity.omos.viewmodels.MyScrapRecordViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*

class BlockingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlockingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocking)
        initToolBar()

        val adapter = BlockedListAdapter()
        binding.recyclerView.adapter = adapter

    }

    private fun subscribeUi(adapter: BlockedListAdapter) {

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