package com.infinity.omos

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.infinity.omos.databinding.ActivityUserRecordDetailBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.UserRecordDetailViewModel
import kotlinx.android.synthetic.main.activity_register.*

class UserRecordDetailActivity : AppCompatActivity() {

    private val viewModel: UserRecordDetailViewModel by viewModels()
    private lateinit var binding: ActivityUserRecordDetailBinding

    private val userId = GlobalApplication.prefs.getInt("userId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_record_detail)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        var postId = intent.getIntExtra("postId", -1)

        viewModel.setDetailRecord(postId, userId)
        viewModel.getDetailRecord().observe(this) { record ->
            record?.let {
                binding.data = it
                binding.tvArtist.text = GlobalFunction.setArtist(it.music.artists)
                binding.tvDate.text = GlobalFunction.setDate(it.createdDate)
                binding.tvCategory.text = GlobalFunction.categoryEngToKr(this, it.category)
                binding.tvLikeCnt.text = String.format("%03d", it.likeCnt)
                binding.tvScrapCnt.text = String.format("%03d", it.scrapCnt)

                if (it.category == "A_LINE"){
                    binding.tvAlineContents.visibility = View.VISIBLE
                    binding.tvRecordContents.visibility = View.GONE
                    binding.tvAlineContents.text = "\"${it.recordContents}\""
                } else {
                    binding.tvRecordContents.visibility = View.VISIBLE
                    binding.tvAlineContents.visibility = View.GONE
                }
            }
        }

        viewModel.getStateDetailRecord().observe(this) { state->
            state?.let {
                when(it){
                    Constant.ApiState.LOADING -> {
                        binding.linearLayout.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Constant.ApiState.DONE -> {
                        binding.linearLayout.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }

                    Constant.ApiState.ERROR -> {

                    }
                }
            }
        }

        initToolBar()
    }

    private fun initToolBar(){
        toolbar.title = ""
        setSupportActionBar(toolbar) // 툴바 사용

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_action_record, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_instar -> {

                true
            }
            R.id.action_more -> {
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}