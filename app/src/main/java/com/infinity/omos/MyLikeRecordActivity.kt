package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity.omos.adapters.MyRecordListAdapter
import com.infinity.omos.databinding.ActivityMyLikeRecordBinding
import com.infinity.omos.databinding.ActivityMyScrapRecordBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.MyLikeRecordViewModel
import com.infinity.omos.viewmodels.MyScrapRecordViewModel
import kotlinx.android.synthetic.main.activity_register_nick.*

class MyLikeRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyLikeRecordBinding
    private val viewModel: MyLikeRecordViewModel by viewModels()

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_like_record)

        initToolBar()

        val mAdapter = MyRecordListAdapter(this)
        binding.recyclerView.apply{
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.setLikeRecord(userId)
        viewModel.getLikeRecord().observe(this) { record ->
            record?.let {
                mAdapter.setDjRecord(it)

                if (it.isEmpty()){
                    binding.lnNorecord.visibility = View.VISIBLE
                }
            }
        }
        viewModel.getStateLikeRecord().observe(this) { state ->
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
    }

    private fun initToolBar(){
        toolbar.title = "공감한 레코드"
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action, menu)
        val actionWrite = menu.findItem(R.id.action_write)
        actionWrite.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}