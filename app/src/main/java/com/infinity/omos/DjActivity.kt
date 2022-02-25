package com.infinity.omos

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.ActivityDjBinding
import com.infinity.omos.databinding.ActivitySelectCategoryBinding
import com.infinity.omos.viewmodels.DjViewModel
import kotlinx.android.synthetic.main.activity_register.*

class DjActivity : AppCompatActivity() {

    private val viewModel: DjViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityDjBinding>(this, R.layout.activity_dj)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        initToolBar()

        val mAdapter = MyRecordListAdapter(this)
        binding.rvRecord.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
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