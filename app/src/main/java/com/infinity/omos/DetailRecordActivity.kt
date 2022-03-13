package com.infinity.omos

import android.content.Intent
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
import com.infinity.omos.utils.ShareInstagram
import com.infinity.omos.viewmodels.DetailRecordViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.list_detail_category_item.view.*

class DetailRecordActivity : AppCompatActivity() {

    private val viewModel: DetailRecordViewModel by viewModels()
    private lateinit var binding: ActivityDetailRecordBinding

    private val userId = GlobalApplication.prefs.getInt("userId")
    private lateinit var actionInsta: MenuItem
    private lateinit var actionMore: MenuItem

    private var heart = false
    private var star = false
    private var prevHeart = false
    private var prevStar = false
    private var heartCnt = 0
    private var starCnt = 0
    private var toUserId = -1

    private var postId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_record)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        postId = intent.getIntExtra("postId", -1)

        viewModel.setDetailRecord(postId, userId)
        viewModel.getDetailRecord().observe(this) { record ->
            record?.let {
                binding.data = it
                binding.tvArtist.text = GlobalFunction.setArtist(it.music.artists)
                binding.tvDate.text = GlobalFunction.setDate(it.createdDate)
                binding.tvCategory.text = GlobalFunction.categoryEngToKr(this, it.category)
                binding.tvHeartCnt.text = String.format("%03d", it.likeCnt)
                binding.tvStarCnt.text = String.format("%03d", it.scrapCnt)

                prevHeart = it.isLiked
                prevStar = it.isScraped
                heartCnt = it.likeCnt
                starCnt = it.scrapCnt
                toUserId = it.userId

                // 좋아요 상태
                heart = if (it.isLiked){
                    binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_checked_heart))
                    binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                    true
                } else{
                    binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unchecked_heart))
                    binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                    false
                }

                // 스크랩 상태
                star = if (it.isScraped){
                    binding.imgStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_checked_star))
                    binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                    true
                } else{
                    binding.imgStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unchecked_star))
                    binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                    false
                }

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
                    actionInsta.isVisible = it.category == "A_LINE"
                    actionMore.isVisible = true
                } else{
                    binding.btnPublic.visibility = View.GONE
                    binding.btnReport.visibility = View.VISIBLE
                    actionInsta.isVisible = false
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

        // 좋아요 클릭
        binding.btnHeart.setOnClickListener {
            if (heart){
                binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unchecked_heart))
                binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                heartCnt -= 1
                heart = false
            } else{
                binding.imgHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_checked_heart))
                binding.tvHeartCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                heartCnt += 1
                heart = true
            }
            binding.tvHeartCnt.text = String.format("%03d", heartCnt)
        }

        // 스크랩 클릭
        binding.btnStar.setOnClickListener {
            if (star){
                binding.imgStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unchecked_star))
                binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.gray_03))
                starCnt -= 1
                star = false
            } else{
                binding.imgStar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_checked_star))
                binding.tvStarCnt.setTextColor(ContextCompat.getColor(this, R.color.orange))
                starCnt += 1
                star = true
            }
            binding.tvStarCnt.text = String.format("%03d", starCnt)
        }

        // DJ 클릭
        binding.btnDj.setOnClickListener {
            val intent = Intent(this, DjActivity::class.java)
            intent.putExtra("toUserId", toUserId)
            startActivity(intent)
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
        actionInsta = menu.findItem(R.id.action_insta)
        actionMore = menu.findItem(R.id.action_more)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_insta -> {
                val insta = ShareInstagram(this)
                insta.shareInsta(binding.imgAlbumCover, binding.tvRecordTitle)
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

    override fun onDestroy() {
        super.onDestroy()

        // 좋아요, 스크랩 처리
        if (heart != prevHeart){
            if (heart){
                viewModel.saveLike(postId, userId)
            } else{
                viewModel.deleteLike(postId, userId)
            }
        }

        if (star != prevStar){
            if (star){
                viewModel.saveScrap(postId, userId)
            } else{
                viewModel.deleteScrap(postId, userId)
            }
        }
    }
}