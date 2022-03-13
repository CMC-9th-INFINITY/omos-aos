package com.infinity.omos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.infinity.omos.databinding.ActivityDetailRecordBinding
import com.infinity.omos.etc.Constant
import com.infinity.omos.etc.GlobalFunction
import com.infinity.omos.utils.GlobalApplication
import com.infinity.omos.viewmodels.UserRecordDetailViewModel
import kotlinx.android.synthetic.main.activity_register.*

class DetailRecordActivity : AppCompatActivity() {

    private val viewModel: UserRecordDetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailRecordBinding

    private val userId = GlobalApplication.prefs.getInt("userId")
    private lateinit var actionInstar: MenuItem
    private lateinit var actionMore: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_record)
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

                // 공개/비공개 아이콘 변경
                if (it.isPublic == null || it.isPublic == true){
                    binding.btnPublic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_public))
                } else {
                    binding.btnPublic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_private))
                }

                // 한 줄 감상 구분
                if (it.category == "A_LINE"){
                    binding.tvAlineContents.visibility = View.VISIBLE
                    binding.tvRecordContents.visibility = View.GONE
                    binding.tvAlineContents.text = "\"${it.recordContents}\""
                } else {
                    binding.tvRecordContents.visibility = View.VISIBLE
                    binding.tvAlineContents.visibility = View.GONE
                }

                // 내 레코드인지 확인
                if (it.userId == userId){
                    binding.btnPublic.visibility = View.VISIBLE
                    binding.btnReport.visibility = View.GONE
                    actionInstar.isVisible = it.category == "A_LINE"
                    actionMore.isVisible = true
                } else{
                    binding.btnPublic.visibility = View.GONE
                    binding.btnReport.visibility = View.VISIBLE
                    actionInstar.isVisible = false
                    actionMore.isVisible = false
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
        actionInstar = menu.findItem(R.id.action_instar)
        actionMore = menu.findItem(R.id.action_more)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_instar -> {

                true
            }
            R.id.action_more -> {

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